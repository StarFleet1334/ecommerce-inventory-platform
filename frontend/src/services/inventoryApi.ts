import type { 
  ProductMessage, 
  ApiResponse 
} from '../types/inventory';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api/inventory';

class InventoryApiService {
  private async request<T>(
    endpoint: string, 
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      // For DELETE operations, the response might be empty
      if (response.status === 204) {
        return {} as T;
      }
      
      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // Product APIs
  async createProduct(product: ProductMessage, initialLoad: boolean = false): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/product?initialLoad=${initialLoad}`, {
      method: 'POST',
      body: JSON.stringify(product),
    });
  }

  async deleteProduct(productId: string): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/product/${productId}`, {
      method: 'DELETE',
    });
  }

  // Customer APIs
  async createCustomer(customer: any, initialLoad: boolean = false): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/customer?initialLoad=${initialLoad}`, {
      method: 'POST',
      body: JSON.stringify(customer),
    });
  }

  async deleteCustomer(customerId: string): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/customer/${customerId}`, {
      method: 'DELETE',
    });
  }

  // Employee APIs
  async createEmployee(employee: any, initialLoad: boolean = false): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/employee?initialLoad=${initialLoad}`, {
      method: 'POST',
      body: JSON.stringify(employee),
    });
  }

  async deleteEmployee(employeeId: string): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/employee/${employeeId}`, {
      method: 'DELETE',
    });
  }

  // Stock APIs
  async createStock(stock: any, initialLoad: boolean = false): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/stock?initialLoad=${initialLoad}`, {
      method: 'POST',
      body: JSON.stringify(stock),
    });
  }

  async deleteStock(stockId: string): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/stock/${stockId}`, {
      method: 'DELETE',
    });
  }

  // Supplier APIs
  async createSupplier(supplier: any, initialLoad: boolean = false): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/supplier?initialLoad=${initialLoad}`, {
      method: 'POST',
      body: JSON.stringify(supplier),
    });
  }

  async deleteSupplier(supplierId: string): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/supplier/${supplierId}`, {
      method: 'DELETE',
    });
  }

  // Warehouse APIs
  async createWarehouse(warehouse: any, initialLoad: boolean = false): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/warehouse?initialLoad=${initialLoad}`, {
      method: 'POST',
      body: JSON.stringify(warehouse),
    });
  }

  async deleteWarehouse(warehouseId: string): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/warehouse/${warehouseId}`, {
      method: 'DELETE',
    });
  }

  // Supply APIs
  async createSupply(supply: any, initialLoad: boolean = false): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/supply?initialLoad=${initialLoad}`, {
      method: 'POST',
      body: JSON.stringify(supply),
    });
  }

  async deleteSupply(supplyId: string): Promise<ApiResponse> {
    return this.request<ApiResponse>(`/api/v1/supply/${supplyId}`, {
      method: 'DELETE',
    });
  }
}

export const inventoryApi = new InventoryApiService();
export default inventoryApi; 