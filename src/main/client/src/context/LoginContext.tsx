import React, {createContext, ReactNode, useContext, useEffect, useState} from "react";
import {Env} from "../Env.ts";

interface AuthResponse {
    access_token: string;
    refresh_token: string;
    username: string;
}

interface AuthContextType {
    username: string | null;
    login: (username: string, password: string) => Promise<{ success: boolean; message?: string }>;
    logout: () => Promise<void>;
    authenticate: (data: AuthResponse) => void;
}

const AuthContext = createContext<AuthContextType>({
    username: null,
    login: async () => ({success: false}),
    logout: async () => {
    },
    authenticate: () => {
    }
});

export const useAuth = () => useContext(AuthContext);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [user, setUser] = useState<string | null>(null);

    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    const authenticate = (data: AuthResponse) => {
        localStorage.setItem('access_token', data.access_token);
        localStorage.setItem('refresh_token', data.refresh_token);
        localStorage.setItem('user', JSON.stringify(data.username));
        setUser(data.username);
    };

    const login = async (username: string, password: string): Promise<{ success: boolean; message?: string }> => {
        try {
            const response = await fetch(`${Env.API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({username, password})
            });

            if (!response.ok) {
                throw new Error("Wrong username or password !");
            }

            const data: AuthResponse = await response.json();

            authenticate(data);
            return {success: true};

        } catch (error) {
            console.error('Login failed:', error);
            return {success: false, message: 'Wrong username or password !'};
        }
    };

    const logout = async (): Promise<void> => {
        const accessToken = localStorage.getItem('access_token');

        try {
            await fetch(`${Env.API_BASE_URL}/auth/logout`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                }
            });
        } catch (error) {
            console.error('Error calling logout endpoint:', error);
        } finally {
            localStorage.removeItem('access_token');
            localStorage.removeItem('refresh_token');
            localStorage.removeItem('user');
            setUser(null);
        }
    };

    return (
        <AuthContext.Provider value={{ username: user, login, logout, authenticate }}>
            {children}
        </AuthContext.Provider>
    );
};
