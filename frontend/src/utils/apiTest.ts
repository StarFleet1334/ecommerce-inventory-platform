import inventoryApi from '../services/inventoryApi';

export const testApiConnection = async () => {
  try {
    console.log('Testing API connection...');
    
    // Test product creation
    const testProduct = {
      product_name: 'API Test Product',
      sku: 'TEST001',
      product_id: 'TEST_' + Date.now(),
      product_price: 9.99,
      product_description: 'This is a test product to verify API connectivity'
    };

    console.log('Creating test product:', testProduct);
    const createResponse = await inventoryApi.createProduct(testProduct, false);
    console.log('Create response:', createResponse);

    // Test product deletion
    console.log('Deleting test product:', testProduct.product_id);
    const deleteResponse = await inventoryApi.deleteProduct(testProduct.product_id);
    console.log('Delete response:', deleteResponse);

    console.log('✅ API connection test successful!');
    return true;
  } catch (error) {
    console.error('❌ API connection test failed:', error);
    return false;
  }
};

export const checkApiHealth = async () => {
  try {
    const response = await fetch('/api/inventory/actuator/health');
    if (response.ok) {
      const health = await response.json();
      console.log('API Health:', health);
      return health.status === 'UP';
    }
    return false;
  } catch (error) {
    console.error('Health check failed:', error);
    return false;
  }
}; 