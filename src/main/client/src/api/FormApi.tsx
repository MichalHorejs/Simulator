import { Env } from "../Env.ts";

export interface Category {
    name: string;
    displayName: string;
}

export interface Subcategory {
    name: string;
    displayName: string;
}

const getCategories = async (): Promise<Category[]> => {
    const response = await fetch(`${Env.API_BASE_URL}/enums/category`);
    if (!response.ok) {
        throw new Error("Chyba při načítání kategorií");
    }
    return await response.json();
};

const getSubcategories = async (category: string): Promise<Subcategory[]> => {
    const response = await fetch(
        `${Env.API_BASE_URL}/enums/subcategory?category=${encodeURIComponent(category)}`
    );
    if (!response.ok) {
        throw new Error("Chyba při načítání podkategorií");
    }
    return await response.json();
};

const getDistricts = async (): Promise<string[]> => {
    const response = await fetch(`${Env.API_BASE_URL}/incident-template/district`);
    if (!response.ok) {
        throw new Error("Chyba při načítání okresů");
    }
    return await response.json();
}

const getMunicipalities = async (district: string): Promise<string[]> => {
    const response = await fetch(`${Env.API_BASE_URL}/incident-template/municipality?district=${encodeURIComponent(district)}`);
    if (!response.ok) {
        throw new Error("Chyba při načítání vesnic");
    }
    return await response.json();
}
export { getCategories, getSubcategories, getDistricts, getMunicipalities };