// src/main/client/src/components/simulation-result/SimulationResult.tsx
import React from "react";
import Accordion from "react-bootstrap/Accordion";
import { SimulationResultsDTO } from "../../pages/SimulationResultsPage";
import "./SimulationResult.css";

const getUrgencyText = (urgency: string): string => {
    switch (urgency) {
        case "HIGH":
            return "Vysoká";
        case "MEDIUM":
            return "Střední";
        case "LOW":
            return "Nízká";
        default:
            return urgency;
    }
};

const SimulationResult: React.FC<{ results: SimulationResultsDTO }> = ({ results }) => {
    const renderRow = (
        label: string,
        chosen: string | undefined,
        correct: string | undefined
    ) => {
        const chosenValue =
            chosen == null || chosen.trim() === ""
                ? "nevyplněno"
                : label === "Naléhavost"
                    ? getUrgencyText(chosen)
                    : chosen;
        const correctValue =
            correct == null || correct.trim() === ""
                ? "nevyplněno"
                : label === "Naléhavost"
                    ? getUrgencyText(correct)
                    : correct;
        const chosenCss =
            chosen == null || chosen.trim() === ""
                ? "no-data"
                : chosenValue === correctValue
                    ? "match"
                    : "mismatch";
        return (
            <tr>
                <td className="label-cell">{label}</td>
                <td className={chosenCss}>{chosenValue}</td>
                <td className="label-cell">Správně</td>
                <td>{correctValue}</td>
            </tr>
        );
    };

    const renderVehiclesRow = (chosenVehicles: string[], correctVehicles: string[]) => {
        const chosenData = chosenVehicles && chosenVehicles.length > 0 ? chosenVehicles : ["nevyplněno"];
        const correctData = correctVehicles && correctVehicles.length > 0 ? correctVehicles : ["nevyplněno"];
        return (
            <tr>
                <td className="label-cell">Vozidla</td>
                <td>
                    {chosenData.map((vehicle, index) => {
                        const cssClass =
                            vehicle === "nevyplněno"
                                ? "no-data"
                                : correctData.includes(vehicle)
                                    ? "match"
                                    : "mismatch";
                        return (
                            <span key={index} className={cssClass}>
                                {vehicle}{index < chosenData.length - 1 ? ", " : ""}
                            </span>
                        );
                    })}
                </td>
                <td className="label-cell">Správně</td>
                <td>{correctData.join(", ")}</td>
            </tr>
        );
    };

    const getDistanceClass = (distance: number) => {
        if (distance <= 30) return "distance-green";
        if (distance < 125) return "distance-yellow";
        return "distance-red";
    };

    const getDurationClass = (duration: number) => {
        if (duration < 60) return "duration-green";
        if (duration < 180) return "duration-yellow";
        return "duration-red";
    };

    return (
        <div className="simulation-result-table">
            <h2>Skóre hráče {results.username} je: {results.result}</h2>
            <Accordion defaultActiveKey="0">
                {results.incidents.map((incident, idx) => (
                    <Accordion.Item eventKey={idx.toString()} key={idx}>
                        <Accordion.Header>Incident {idx + 1}</Accordion.Header>
                        <Accordion.Body>
                            <table className="result-table">
                                <tbody>
                                {renderRow("Kategorie", incident.chosenCategory, incident.correctCategory)}
                                {renderRow("Subkategorie", incident.chosenSubcategory, incident.correctSubcategory)}
                                {renderRow("Naléhavost", incident.chosenUrgency, incident.correctUrgency)}
                                {renderRow("Okres", incident.chosenDistrict, incident.correctDistrict)}
                                {renderRow("Obec", incident.chosenMuncipality, incident.correctMuncipality)}
                                {renderVehiclesRow(incident.chosenVehicleTypes, incident.correctVehicleTypes)}
                                <tr>
                                    <td className="label-cell">Vzdálenost</td>
                                    <td
                                        className={
                                            incident.distance == null
                                                ? "no-data"
                                                : getDistanceClass(incident.distance)
                                        }
                                        colSpan={3}
                                    >
                                        {incident.distance == null ? "nevyplněno" : `${incident.distance} m`}
                                    </td>
                                </tr>
                                <tr>
                                    <td className="label-cell">Čas k vyzvednutí</td>
                                    <td
                                        className={
                                            incident.durationToPickUp == null
                                                ? "no-data"
                                                : getDurationClass(incident.durationToPickUp)
                                        }
                                        colSpan={3}
                                    >
                                        {incident.durationToPickUp == null ? "nevyplněno" : `${incident.durationToPickUp} s`}
                                    </td>
                                </tr>
                                <tr>
                                    <td className="label-cell">Čas na obsluhu</td>
                                    <td
                                        className={
                                            incident.durationToServeIncident == null
                                                ? "no-data"
                                                : getDurationClass(incident.durationToServeIncident)
                                        }
                                        colSpan={3}
                                    >
                                        {incident.durationToServeIncident == null ? "nevyplněno" : `${incident.durationToServeIncident} s`}
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </Accordion.Body>
                    </Accordion.Item>
                ))}
            </Accordion>
        </div>
    );
};

export default SimulationResult;