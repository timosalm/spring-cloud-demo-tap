export const environment = {
  production: true,
  baseHref: '/frontend/',
  authConfig: {
    issuer: 'CHANGE_ME',
    clientId: 'dev-space_client-registration'
  },
  endpoints: {
    orders: window.location.origin + '/services/order-service/api/v1/orders',
    products: window.location.origin +  '/services/product-service/api/v1/products'
  }
};
