import React from "react";
import Accordion from "react-bootstrap/Accordion";
import { SimulationResultsDTO } from "../../pages/SimulationResultsPage";
import "./SimulationResult.css";

const SimulationResult: React.FC<{ results: SimulationResultsDTO }> = ({ results }) => {
    const renderRow = (label: string, chosen: string | undefined, correct: string | undefined) => {
        const chosenValue = chosen && chosen.trim() !== "" ? chosen : "nevyplněno";
        const correctValue = correct && correct.trim() !== "" ? correct : "nevyplněno";
        const match = chosenValue === correctValue;
        return (
            <tr>
                <td className="label-cell">{label}</td>
                <td className={match ? "match" : "mismatch"}>{chosenValue}</td>
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
                        const isCorrect = correctData.includes(vehicle);
                        return (
                            <span key={index} className={isCorrect ? "match" : "mismatch"}>
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
            <h2>Vaše skore je: {results.result}</h2>
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
                                    <td className={getDistanceClass(incident.distance)} colSpan={3}>
                                        {incident.distance} m
                                    </td>
                                </tr>
                                <tr>
                                    <td className="label-cell">Čas k vyzvednutí</td>
                                    <td className={getDurationClass(incident.durationToPickUp)} colSpan={3}>
                                        {incident.durationToPickUp} s
                                    </td>
                                </tr>
                                <tr>
                                    <td className="label-cell">Čas na obsluhu</td>
                                    <td className={getDurationClass(incident.durationToServeIncident)} colSpan={3}>
                                        {incident.durationToServeIncident} s
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