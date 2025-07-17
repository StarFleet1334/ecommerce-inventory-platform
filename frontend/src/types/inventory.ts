export interface Product {
  product_name: string;
  sku: string;
  product_id: string;
  product_price: number;
  product_description: string;
}

export interface ProductMessage {
  product_name: string;
  sku: string;
  product_id: string;
  product_price: number;
  product_description: string;
}

export interface ApiResponse {
  message: string;
  success: boolean;
}

export interface Customer {
  customer_id: string;
  customer_name: string;
  email: string;
  phone: string;
  address: string;
}

export interface Employee {
  employee_id: string;
  employee_name: string;
  email: string;
  phone: string;
  position: string;
  department: string;
}

export interface Stock {
  stock_id: string;
  product_id: string;
  quantity: number;
  warehouse_id: string;
  last_updated: string;
}

export interface Supplier {
  supplier_id: string;
  supplier_name: string;
  email: string;
  phone: string;
  address: string;
}

export interface Warehouse {
  warehouse_id: string;
  warehouse_name: string;
  location: string;
  capacity: number;
}

export interface Supply {
  supply_id: string;
  supplier_id: string;
  product_id: string;
  quantity: number;
  supply_date: string;
} 