import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Order} from "./order.entity";
import {environment} from "../../environments/environment";
import {Product} from "./product.entity";

@Injectable({
  providedIn: 'root',
})
export class OrderService {

  constructor(private httpClient: HttpClient) {
  }

  fetchProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(environment.endpoints.products);
  }

  fetchOrders(): Observable<Order[]> {
    return this.httpClient.get<Order[]>(environment.endpoints.orders);
  }

  createOrder(newOrder: Order): Observable<Order> {
    return this.httpClient.post<Order>(environment.endpoints.orders, newOrder);
  }
}
