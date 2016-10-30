import {Component, AfterViewInit} from '@angular/core';
import {DiveComputerService} from "./dive-computer.service";
import {DivePlan} from "./models/dive-plan";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {

    dive: any;
    overSat: any;
    diveChartOptions: any;
    overSatChartOptions: any;

    constructor(private diveComputer: DiveComputerService) {
        this.dive = {
            datasets: [{
                label: 'Scatter Dataset',
                data: [{x: -10,y: 0}, {x: 0,y: 10}, {x: 10,y: 5}]
            }]
        };
        this.overSat = {
            datasets: [{
                label: 'Scatter Dataset',
                data: [{x: -10,y: 0}, {x: 0,y: 10}, {x: 10,y: 5}]
            }]
        };

        this.diveChartOptions = {
            scales: {
                xAxes: [{
                    type: 'linear',
                    position: 'bottom'
                }]
            },
            maintainAspectRatio: false
        };

        this.overSatChartOptions = {
            scales: {
                xAxes: [{
                    type: 'linear',
                    position: 'bottom'
                }]
            },
            maintainAspectRatio: false
        };
    }

    ngAfterViewInit(): void {
        this.diveComputer.divePlan.subscribe((divePlan: DivePlan)=> {
            let depthData = [];
            for (let divePoint of divePlan.theDive) {
                depthData.push({x: divePoint.date, y: -divePoint.depth});
            }
            let overSatData = [];
            for (let overSatPoint of divePlan.distanceToOverSaturation) {
                overSatData.push({x: overSatPoint.date, y: overSatPoint.dist});
            }

            this.dive = {
                datasets: [{
                    label: 'Profil de plongée',
                    data: depthData,
                    cubicInterpolationMode: 'monotone'
                }]
            };
            this.overSat ={
                datasets: [{
                    label: "Distance à sur saturation",
                    data: overSatData,
                    cubicInterpolationMode: 'monotone'
                }]
            };
        });
    }
}
