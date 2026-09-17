"use client";

import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";

import api from "@/lib/api";

interface User {
  id: string;
  name: string;
  email: string;
}

interface AuthContextType {
  user: User | null;
  loading: boolean;
  refreshUser: () => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);


  const refreshUser = async () => {
  try {
    const response = await api.get<User>("/me");

    setUser(response.data);
  } catch {
    setUser(null);
  }
};

  useEffect(() => {

    const checkUser = async () => {
    await refreshUser();
    setLoading(false);
  };

    checkUser();
  }, []);

  const logout = async () => {
    try {
      await api.post("/logout");
      setUser(null);
    } catch {
      console.error("Logout failed");
    }
  };

  return (
    <AuthContext.Provider value={{ user, refreshUser, loading, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }

  return context;
}