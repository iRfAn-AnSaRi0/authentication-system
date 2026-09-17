"use client";

import { useRouter } from "next/navigation";

import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import { useAuth } from "@/context/AuthContext";

export default function UserMenu() {
    const { user, logout } = useAuth();
    const router = useRouter();

    const handleLogout = async () => {
        await logout();
        router.replace("/auth");
    };

    if (!user) {
        return null;
    }

    const initials = user.name
        .split(" ")
        .map((name) => name[0])
        .join("")
        .toUpperCase()
        .slice(0, 2);

    return (
        <DropdownMenu>
  <DropdownMenuTrigger className="rounded-full outline-none">
    <Avatar>
      <AvatarFallback>{initials}</AvatarFallback>
    </Avatar>
  </DropdownMenuTrigger>

  <DropdownMenuContent align="end" className="w-56">
    <div className="px-2 py-1.5 text-sm text-muted-foreground">
      {user.name}
    </div>
    <div className="px-2 py-1.5 text-sm text-muted-foreground">
      {user.email}
    </div>

    <DropdownMenuSeparator />

    <DropdownMenuItem onClick={handleLogout}>
      Logout
    </DropdownMenuItem>
  </DropdownMenuContent>
</DropdownMenu>
    );
}