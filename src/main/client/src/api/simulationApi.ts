// // src/main/client/src/api/simulationApi.ts
// import {Env} from "../Env.ts";
//
// export interface ISimulationStart {
//     difficulty: number;
//     person: {
//         username: string;
//     };
// }
//
// export const startSimulation = async (simulation: ISimulationStart) => {
//     const response = await fetch(`${Env.API_BASE_URL}/simulation/start`, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(simulation),
//     });
//
//     if (!response.ok) {
//         console.error(response);
//         throw new Error('Chyba při spuštění simulace');
//     }
//     return await response.json();
// };

import {Env} from "../Env.ts";

const startSimulation = async (username: string, difficulty: number) => {
    const payload = {
        difficulty,
        person: {username},
    }

    const response = await fetch(`${Env.API_BASE_URL}/simulation/start`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        console.error(response);
        throw new Error('Chyba při spuštění simulace');
    }
    return await response.json();
}

export { startSimulation };