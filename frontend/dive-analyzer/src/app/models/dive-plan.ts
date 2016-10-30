import {DivePoint} from "./dive-point";

export class OverSatPoint{
    date: number;
    dist: number;
}

export class DivePlan{
    theDive: DivePoint[];
    distanceToOverSaturation: OverSatPoint[];
}