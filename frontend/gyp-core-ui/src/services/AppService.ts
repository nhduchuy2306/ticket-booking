import type { MenuProps } from "antd";
import { ItemType, SubMenuType } from "antd/es/menu/interface";
import React from "react";

export type MenuItem = Required<MenuProps>['items'][number] & {
    label: string;
    key: React.Key;
    appId?: string;
    children?: MenuItem[]
}

export function getItem(label: React.ReactNode, key: React.Key, appId: string, icon?: React.ReactNode, children?: MenuItem[]): MenuItem {
    return {key, icon, children, label, appId} as MenuItem;
}

export const hasChildren = (item: MenuItem) => item && Array.isArray(item.children);

export const findMenuPath = (items: MenuItem[] | (ItemType[] & MenuItem[]) | undefined, key: string, path: string[] = []): string[] | null => {
    if (!items) {
        return null;
    }
    for (const item of items) {
        if (!item || typeof item !== 'object') {
            continue;
        }
        if (item.key === key) {
            return [...path, String(item.key)];
        }
        if (hasChildren(item)) {
            const result = findMenuPath(item.children, key, [...path, String(item.key)]);
            if (result) {
                return result;
            }
        }
    }
    return null;
};

export const getLabelByKey = (key: string, items: MenuItem[]): string | null => {
    for (const item of items) {
        if (item.key === key) return String(item.label);
        if ((item as SubMenuType).children) {
            const children = (item as SubMenuType).children;
            const found = getLabelByKey(key, children as MenuItem[]);
            if (found) return found;
        }
    }
    return null;
};

export const hasPermission = (
        key: React.Key,
        permissions: Map<string, string[]>
) => {
    const actions = permissions.get(key as string);
    return actions && actions.includes?.('READ');
};

export const filterMenu = (
        isAdministrator: boolean,
        items: MenuItem[],
        permissions: Map<string, string[]>
): MenuItem[] => {
    if (isAdministrator) {
        return items;
    }
    return items
            .map(item => {
                if (item.children) {
                    const filteredChildren = filterMenu(isAdministrator, item.children, permissions);

                    if (filteredChildren.length > 0) {
                        return {
                            ...item,
                            children: filteredChildren,
                        };
                    }

                    return null;
                }

                if (hasPermission(item.key, permissions)) {
                    return item;
                }

                return null;
            })
            .filter(Boolean) as MenuItem[];
};
