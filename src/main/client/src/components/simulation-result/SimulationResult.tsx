import {Accordion} from "react-bootstrap";
import React from "react";

interface IncidentResultsDTO {
    chosenCategory: string;
    correctCategory: string;
}


interface SimulationResultsDTO {
    username: string;
    result: number;
    difficulty: string;
    incidents: IncidentResultsDTO[];
}

const SimulationResults: React.FC<{ results: SimulationResultsDTO }> = ({results}) => {

    return (
        <div>
            <h2>Vaše skore je: {results.result}</h2>
            <Accordion defaultActiveKey="0">
                {results.incidents.map((incident, index) => (
                    <Accordion.Item eventKey={index.toString()} key={index}>
                        <Accordion.Header>Incident {index + 1}</Accordion.Header>
                        <Accordion.Body>
                            <p>Vybraný atribut: {incident.chosenCategory}</p>
                            <p>Správný atribut: {incident.correctCategory}</p>
                        </Accordion.Body>
                    </Accordion.Item>
                ))}
            </Accordion>
        </div>
    );
};

export default SimulationResults;