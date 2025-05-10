import React, { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import { getCategories, Category, getSubcategories, Subcategory, getDistricts, getMunicipalities } from "../../api/FormApi";
import "./Form.css"

interface FormData {
    category: string;
    subcategory: string;
    district: string;
    municipality: string;
    urgency: string;
    cars: string[];
    specification: string;
}

interface FormProps {
    incidentId: string;
}

const Form: React.FC<FormProps> = ({ incidentId }) => {
    const [formData, setFormData] = useState<FormData>({
        category: "",
        subcategory: "",
        district: "",
        municipality: "",
        urgency: "",
        cars: [],
        specification: ""
    });

    const [categories, setCategories] = useState<Category[]>([]);
    const [subcategories, setSubcategories] = useState<Subcategory[]>([]);
    const [districts, setDistricts] = useState<string[]>([]);
    const [municipalities, setMunicipalities] = useState<string[]>([]);


    useEffect(() => {
        getCategories()
            .then((data: React.SetStateAction<Category[]>) => setCategories(data))
            .catch(error => console.error("Chyba při načítání kategorií:", error));
    }, []);

    useEffect(() => {
        if (formData.category) {
            getSubcategories(formData.category)
                .then(data => setSubcategories(data))
                .catch(error => console.error("Chyba při načítání podkategorií:", error));
        } else {
            setSubcategories([]);
        }
    }, [formData.category]);

    useEffect(() => {
        getDistricts()
            .then(data => setDistricts(data))
            .catch(error => console.error("Chyba při načítání okresů:", error));
    }, []);

    useEffect(() => {
        if (formData.district) {
            getMunicipalities(formData.district)
                .then(data => setMunicipalities(data))
                .catch(error => console.error("Chyba při načítání vesnic:", error));
        } else {
            setMunicipalities([]);
        }
    }, [formData.district]);

    const handleChange = (
        e: React.ChangeEvent<HTMLSelectElement | HTMLInputElement | HTMLTextAreaElement>
    ) => {
        const { name, value } = e.target;
        if (name === "cars") {
            const options = (e.target as HTMLSelectElement).selectedOptions;
            const values = Array.from(options).map(option => option.value);
            setFormData({ ...formData, [name]: values });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const payload = { incidentId, ...formData };
        console.log("Odesláno:", payload);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="top-container">
                <div>
                    <label>Kategorie:</label><br/>
                    <select name="category" value={formData.category} onChange={handleChange} required>
                        <option value="" disabled hidden>Vyberte kategorii</option>
                        {categories.map(cat => (
                            <option key={cat.name} value={cat.name}>
                                {cat.displayName}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Podkategorie:</label><br/>
                    <select name="subcategory" value={formData.subcategory} onChange={handleChange} required>
                        <option value="">Vyberte podkategorii</option>
                        {subcategories.map(sub => (
                            <option key={sub.name} value={sub.name}>
                                {sub.displayName}
                            </option>
                        ))}
                    </select>
                </div>
            </div>
            <div className="mid-container">
                <div>
                    <label>Okres:</label><br/>
                    <select name="district" value={formData.district} onChange={handleChange} required>
                        <option value="" disabled hidden>Vyberte okres</option>
                        {districts.map(district => (
                            <option key={district} value={district}>
                                {district}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Vesnice:</label><br/>
                    <select name="municipality" value={formData.municipality} onChange={handleChange} required>
                        <option value="" disabled hidden>Vyberte vesnici</option>
                        {municipalities.map(muni => (
                            <option key={muni} value={muni}>
                                {muni}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Naléhavost:</label><br/>
                    <select name="urgency" value={formData.urgency} onChange={handleChange} required>
                        <option value="" disabled hidden>Vyberte Naléhavost</option>
                        <option value="0">Nízká</option>
                        <option value="1">Střední</option>
                        <option value="2">Velká</option>
                    </select>
                </div>
            </div>
            <div className="bot-container">
                <div>
                    <label>Podrobnosti o incidentu:</label><br/>
                    <textarea name="specification"
                              value={formData.specification}
                              onChange={handleChange}
                              placeholder="Napiš podrobnosti o incidentu..."
                              required/>
                </div>
                <div>
                    <label>Vyjezdová auta:</label><br/>
                    <select
                        name="cars"
                        multiple
                        value={formData.cars}
                        onChange={handleChange}
                        required
                    >
                        <option value="auto1">Auto 1</option>
                        <option value="auto2">Auto 2</option>
                        <option value="auto3">Auto 3</option>
                    </select>
                </div>
            </div>
            <Button variant="secondary" type="submit">
                Odeslat
            </Button>
    </form>
    );
};

export default Form;