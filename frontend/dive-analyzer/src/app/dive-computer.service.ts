import {Injectable, EventEmitter} from '@angular/core';
import {DivePoint} from "./models/dive-point";
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import 'rxjs/Rx';
import {DivePlan} from "./models/dive-plan";
import {environment} from '../environments/environment'

@Injectable()
export class DiveComputerService {

    private _divePlan: EventEmitter<DivePlan> = new EventEmitter<DivePlan>();

    constructor(private http: Http) {
    }

    public computeDive(dive: DivePoint[]){
        let body = JSON.stringify(dive);
        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });

        this.http.post(environment.HTTPBaseURL+'/dive-plan', body, options).toPromise().then((res: Response)=>
        {
            this.extractData(res);
        });
    }

    private extractData(res: Response){
        let divePlan = res.json() as DivePlan;
        this._divePlan.emit(divePlan);
        console.log("Dive received : " + this._divePlan);
    }

    get divePlan(): EventEmitter<DivePlan> {
        return this._divePlan;
    }
}
