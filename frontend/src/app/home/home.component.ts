import { Component } from '@angular/core';
import {Order} from "./order.entity";
import {HomeService} from "./home.service";
import {Product} from "./product.entity";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'home',
  templateUrl: './home.component.html'
})
export class HomeComponent {

  createOrderModalVisible = false;
  createOrderForm?: FormGroup;

  orders: Order[] = [];
  products: Product[] = [];

  constructor(private homeService: HomeService, private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.fetchProducts();
    this.fetchOrders();
  }

  getProductName(productId: number): string {
    return this.products.find(product => product.id === productId)?.name || 'Unknown';
  }

  createOrderModal() {
    this.createOrderForm = this.formBuilder.group({
      productId: [null, [Validators.required, Validators.min(1), Validators.max(9999)]],
      shippingAddress: [null , [Validators.required, Validators.minLength(1) , Validators.maxLength(100)]]
    });
    this.createOrderModalVisible = true;
  }

  createOrder() {
    if (this.createOrderForm?.invalid) return;
    this.createOrderModalVisible = false;
    this.homeService.createOrder(this.createOrderForm?.value).subscribe(order => this.orders.push(order));
  }

  fetchOrders() {
    this.homeService.fetchOrders().subscribe(orders => this.orders = orders);
  }

  private fetchProducts() {
    this.homeService.fetchProducts().subscribe(products => this.products = products);
  }
}
