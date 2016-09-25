import {Injectable, EventEmitter} from '@angular/core';
import {DivePoint} from "./models/dive-point";
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import {DivePlan} from "./models/dive-plan";
import 'rxjs/Rx';

@Injectable()
export class DiveComputerService {

    private _divePlan: EventEmitter<DivePlan> = new EventEmitter<DivePlan>();

    constructor(private http: Http) {
    }

    public computeDive(dive: DivePoint[]){
        let body = JSON.stringify({ dive });
        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });

        this.http.post('http://127.0.0.1:8080/dive-plan', body, options).toPromise().then(this.extractData);
    }

    private extractData(res: Response){
        let divePlan = res.json().data as DivePlan;
        this._divePlan.emit(divePlan);
        console.log("Dive received : " + this._divePlan);
    }

    get divePlan(): EventEmitter<DivePlan> {
        return this._divePlan;
    }
}
