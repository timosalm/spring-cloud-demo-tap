export const environment = {
  production: true,
  baseHref: '/frontend/',
  authConfig: {
    issuer: 'http://authserver-1-dev-namespace.tap.ryanjbaxter.com',
    clientId: 'dev-namespace_client-registration'
  },
  endpoints: {
    orders: window.location.origin + '/services/order-service/api/v1/orders',
    products: window.location.origin +  '/services/product-service/api/v1/products'
  }
};
