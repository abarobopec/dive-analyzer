import {DivePoint} from "./dive-point";

export class OverSatPoint{
    date: number;
    distance: number;
}

export class DivePlan{
    theDive: DivePoint[];
    distanceToOverSaturation: OverSatPoint[];
}