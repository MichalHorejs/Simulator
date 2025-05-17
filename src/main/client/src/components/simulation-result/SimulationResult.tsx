import {Accordion} from "react-bootstrap";
import React from "react";
import {SimulationResultsDTO} from "../../pages/SimulationResultsPage.tsx";

const SimulationResults: React.FC<{ results: SimulationResultsDTO }> = ({results}) => {

    return (
        <div>
            <br /><br />
            <h2>Vaše skore je: {results.result}</h2>
            <Accordion defaultActiveKey="0">
                {results.incidents.map((incident, index) => (
                    <Accordion.Item eventKey={index.toString()} key={index}>
                        <Accordion.Header>Incident {index + 1}</Accordion.Header>
                        <Accordion.Body>
                            <p>Vybraný atribut: {incident.chosenCategory}</p>
                            <p>Správný atribut: {incident.correctCategory}</p>
                            <p>Vybraná subkategorie: {incident.chosenSubcategory}</p>
                            <p>Správná subkategorie: {incident.correctSubcategory}</p>
                            <p>Vybraná naléhavost: {incident.chosenUrgency}</p>
                            <p>Správná naléhavost: {incident.correctUrgency}</p>
                            <p>Vybraný okres: {incident.chosenDistrict}</p>
                            <p>Správný okres: {incident.correctDistrict}</p>
                            <p>Vybraná obec: {incident.chosenMuncipality}</p>
                            <p>Správná obec: {incident.correctMuncipality}</p>
                            <p>Vybraná vozidla: {incident.chosenVehicleTypes.join(", ")}</p>
                            <p>Správná vozidla: {incident.correctVehicleTypes.join(", ")}</p>
                            <p>Určená vzdálenost od incidentu: {incident.distance} m</p>
                        </Accordion.Body>
                    </Accordion.Item>
                ))}
            </Accordion>
        </div>
    );
};

export default SimulationResults;