import { StatusDTO } from "./status.dto";

export interface CustomerDTO {
    id: number; 
    name: string;
    email: string;
    status?: StatusDTO[]; 
}