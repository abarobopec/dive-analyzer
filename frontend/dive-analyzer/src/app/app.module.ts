import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {DiveInputComponent} from './dive-input/dive-input.component';
import {SpinnerModule, ChartModule} from 'primeng/primeng';
import {DiveComputerService} from "./dive-computer.service";

@NgModule({
    declarations: [
        AppComponent,
        DiveInputComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        SpinnerModule,
        ChartModule
    ],
    providers: [
        DiveComputerService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
