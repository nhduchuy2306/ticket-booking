import type { MenuProps } from "antd";
import React from "react";

export type MenuItem = Required<MenuProps>['items'][number];

export function getItem(label: React.ReactNode, key: React.Key, icon?: React.ReactNode, children?: MenuItem[]): MenuItem {
    return {key, icon, children, label} as MenuItem;
}

export const hasChildren = (item: any): item is { children: MenuItem[] } =>
        item && Array.isArray(item.children);

export const findMenuPath = (items: MenuItem[], key: string, path: string[] = []): string[] | null => {
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
        if (item.children) {
            const found = getLabelByKey(key, item.children);
            if (found) return found;
        }
    }
    return null;
};

