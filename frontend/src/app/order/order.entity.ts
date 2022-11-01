export class Order {
  constructor(public productId: number, public orderStatus: string, public shippingAddress: string, public id?: number) {
  }
}
