import { environment } from '../environments/environment';
import { ConfigService } from './config/config.service';

export function ConfigLoader(configService: ConfigService) {
//Note: this factory need to return a function (that return a promise)

  return () => configService.load(environment.configFile); 
}