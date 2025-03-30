import { Env } from '../Env.ts';

export async function authenticatedFetch(input: RequestInfo, init: RequestInit = {}): Promise<Response> {
    let accessToken = localStorage.getItem('access_token');
    if (!accessToken) {
        window.location.href = '/login';
        return Promise.reject('Chybí access token');
    }

    init.headers = {
        'Content-Type': 'application/json',
        ...init.headers,
        'Authorization': `Bearer ${accessToken}`,
    };

    let response = await fetch(input, init);

    if (response.status !== 401) {
        return response;
    }

    const refreshToken = localStorage.getItem('refresh_token');
    if (!refreshToken) {
        window.location.href = '/login';
        return Promise.reject('Chybí refresh token');
    }

    const refreshResponse = await fetch(`${Env.API_BASE_URL}/auth/refresh`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ refresh_token: refreshToken }),
    });

    if (!refreshResponse.ok) {
        window.location.href = '/login';
        return Promise.reject('Obnova tokenu selhala');
    }

    const tokenData = await refreshResponse.json();

    if (tokenData.access_token && tokenData.refresh_token && tokenData.username) {
        localStorage.setItem('access_token', tokenData.access_token);
        localStorage.setItem('refresh_token', tokenData.refresh_token);
        localStorage.setItem('username', tokenData.username);

        init.headers = {
            'Content-Type': 'application/json',
            ...init.headers,
            'Authorization': `Bearer ${tokenData.access_token}`,
        };

        response = await fetch(input, init);
        return response;
    } else {
        window.location.href = '/login';
        return Promise.reject('Neplatné tokeny');
    }
}