import {Component, OnInit} from '@angular/core';
import {DivePoint} from '../models/dive-point';
import {DiveComputerService} from "../dive-computer.service";

@Component({
    selector: 'dive-input',
    templateUrl: './dive-input.component.html',
    styleUrls: ['./dive-input.component.css'],
})
export class DiveInputComponent implements OnInit {

    public dive: DivePoint[]=[];
    constructor(private diveComputer: DiveComputerService) {
        this.dive.push({date: 0, depth: 0});
        this.dive.push({date: 1, depth: 30});
        this.dive.push({date: 10, depth: 30});
    }

    ngOnInit() {
    }

    public addEmptyPoint(){
        this.dive.push({date: this.dive[this.dive.length-1].date+1,
                        depth: this.dive[this.dive.length-1].depth});
    }

    public computeDive(){
        this.diveComputer.computeDive(this.dive);
    }
}
