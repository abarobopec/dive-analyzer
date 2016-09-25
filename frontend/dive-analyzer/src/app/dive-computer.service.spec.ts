/* tslint:disable:no-unused-variable */

import {TestBed, async, inject} from '@angular/core/testing';
import { MockBackend, MockConnection } from '@angular/http/testing';
import {DiveComputerService} from './dive-computer.service';
import {Response, BaseRequestOptions, Http} from "@angular/http";
import {DivePlan} from "./models/dive-plan";

const mockHttpProvider = {
    deps: [ MockBackend, BaseRequestOptions ],
    useFactory: (backend: MockBackend, defaultOptions: BaseRequestOptions) => {
        return new Http(backend, defaultOptions);
    }
}

describe('Service: DiveComputer', () => {
    /*beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [DiveComputerService, MockBackend, BaseRequestOptions,
                {provide: Http, useFactory: mockHttpProvider}]
        });
    });*/

    it('should publish a new dive plan when requested', inject([DiveComputerService], (service: DiveComputerService) => {
        /*let plan = "";


        service['extractData']()*/
    }));
});
