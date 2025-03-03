import React, {createContext, ReactNode, useContext, useEffect, useState} from "react";
import {Env} from "../Env.ts";

interface AuthResponse {
    access_token: string;
    refresh_token: string;
    username: string;
}

interface AuthContextType {
    user: string | null;
    login: (username: string, password: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
    user: null,
    login: async () => {
    },
    logout: () => {
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

    const login = async (username: string, password: string): Promise<void> => {
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

            localStorage.setItem('access_token', data.access_token);
            localStorage.setItem('refresh_token', data.refresh_token);
            localStorage.setItem('user', JSON.stringify(data.username));

            setUser(data.username);
        } catch (error) {
            console.error('Login failed:', error);
            throw error;
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
        <AuthContext.Provider value={{user, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};
