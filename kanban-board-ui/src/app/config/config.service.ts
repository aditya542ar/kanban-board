import { Injectable } from '@angular/core';
import { Configuration } from './configuration';
import { HttpClient } from '@angular/common/http';

import * as jsonConfig from "../../assets/config/config.json";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private config: Configuration;
  constructor(private http:HttpClient) { }

  load(url:string) {
    return new Promise((resolve) => {
      this.http.get(url)
        .subscribe((config: Configuration) => {
          console.log(config);
          this.config = config;
          resolve();
        }, (err) => {
          console.log(err, jsonConfig, jsonConfig.default);
          this.config = jsonConfig.default as unknown as Configuration;
          resolve();
        });
    });
  }

  getConfiguration():Configuration {
    return this.config;
  }
}
