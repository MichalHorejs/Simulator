export interface ISimulation {
    id: string;
    person?: {
        username: string;
    };
    startTime: Date;
    endTime?: Date;
    rating: number;
    difficulty: string;
}

export class Simulation implements ISimulation {
    public id: string;
    public startTime: Date;
    public endTime?: Date;
    public rating: number;
    public difficulty: string;

    constructor(data: ISimulation) {
        this.id = data.id;
        this.startTime = new Date(data.startTime);
        if (data.endTime) {
            this.endTime = new Date(data.endTime);
        }
        this.rating = data.rating;
        this.difficulty = data.difficulty;
    }

    public isActive(): boolean {
        return !this.endTime;
    }
}