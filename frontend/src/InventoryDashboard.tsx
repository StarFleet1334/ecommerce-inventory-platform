import React, { useState } from 'react';
import inventoryApi from './services/inventoryApi';
import EntityTabs from './components/EntityTabs';
import EntityForm from './components/EntityForm';
import EntityList from './components/EntityList';
import Notification from './components/Notification';
import DebugInfo from './components/DebugInfo';

interface NotificationState {
  show: boolean;
  message: string;
  type: 'success' | 'error' | 'info';
}

// Sample data for demonstration
const sampleData = {
  products: [
    {
      product_name: 'Sample Product',
      sku: 'SKU123',
      product_id: '1',
      product_price: 19.99,
      product_description: 'A sample product for demonstration purposes.'
    }
  ],
  customers: [
    {
      customer_id: 'CUST001',
      customer_name: 'John Doe',
      email: 'john@example.com',
      phone: '+1234567890',
      address: '123 Main St, City, State'
    }
  ],
  employees: [
    {
      employee_id: 'EMP001',
      employee_name: 'Jane Smith',
      email: 'jane@company.com',
      phone: '+1234567891',
      position: 'Manager',
      department: 'Sales'
    }
  ],
  suppliers: [
    {
      supplier_id: 'SUP001',
      supplier_name: 'ABC Supplies',
      email: 'contact@abcsupplies.com',
      phone: '+1234567892',
      address: '456 Business Ave, City, State'
    }
  ],
  warehouses: [
    {
      warehouse_id: 'WH001',
      warehouse_name: 'Main Warehouse',
      location: '789 Industrial Blvd',
      capacity: 10000
    }
  ],
  stocks: [
    {
      stock_id: 'STK001',
      product_id: '1',
      quantity: 100,
      warehouse_id: 'WH001',
      last_updated: new Date().toISOString()
    }
  ],
  supplies: [
    {
      supply_id: 'SUP001',
      supplier_id: 'SUP001',
      product_id: '1',
      quantity: 50,
      supply_date: new Date().toISOString()
    }
  ]
};

const tabs = [
  { id: 'products', label: 'Products', icon: '📦' },
  { id: 'customers', label: 'Customers', icon: '👥' },
  { id: 'employees', label: 'Employees', icon: '👨‍💼' },
  { id: 'suppliers', label: 'Suppliers', icon: '🏢' },
  { id: 'warehouses', label: 'Warehouses', icon: '🏭' },
  { id: 'stocks', label: 'Stock', icon: '📊' },
  { id: 'supplies', label: 'Supplies', icon: '🚚' }
];

const entityConfigs: Record<string, {
  fields: Array<{
    name: string;
    label: string;
    type: 'text' | 'number' | 'email' | 'textarea';
    required?: boolean;
    placeholder?: string;
  }>;
  displayFields: Array<{
    key: string;
    label: string;
    type?: 'text' | 'number' | 'currency' | 'date';
  }>;
  idField: string;
  api: {
    create: (data: any, initialLoad?: boolean) => Promise<any>;
    delete: (id: string) => Promise<any>;
  };
}> = {
  products: {
    fields: [
      { name: 'product_name', label: 'Product Name', type: 'text', required: true },
      { name: 'sku', label: 'SKU', type: 'text', required: true },
      { name: 'product_id', label: 'Product ID', type: 'text', required: true },
      { name: 'product_price', label: 'Price', type: 'number', required: true },
      { name: 'product_description', label: 'Description', type: 'textarea', required: true }
    ],
    displayFields: [
      { key: 'product_name', label: 'Name' },
      { key: 'sku', label: 'SKU' },
      { key: 'product_price', label: 'Price', type: 'currency' },
      { key: 'product_description', label: 'Description' }
    ],
    idField: 'product_id',
    api: {
      create: inventoryApi.createProduct,
      delete: inventoryApi.deleteProduct
    }
  },
  customers: {
    fields: [
      { name: 'customer_id', label: 'Customer ID', type: 'text', required: true },
      { name: 'customer_name', label: 'Name', type: 'text', required: true },
      { name: 'email', label: 'Email', type: 'email', required: true },
      { name: 'phone', label: 'Phone', type: 'text', required: true },
      { name: 'address', label: 'Address', type: 'textarea', required: true }
    ],
    displayFields: [
      { key: 'customer_name', label: 'Name' },
      { key: 'email', label: 'Email' },
      { key: 'phone', label: 'Phone' },
      { key: 'address', label: 'Address' }
    ],
    idField: 'customer_id',
    api: {
      create: inventoryApi.createCustomer,
      delete: inventoryApi.deleteCustomer
    }
  },
  employees: {
    fields: [
      { name: 'employee_id', label: 'Employee ID', type: 'text', required: true },
      { name: 'employee_name', label: 'Name', type: 'text', required: true },
      { name: 'email', label: 'Email', type: 'email', required: true },
      { name: 'phone', label: 'Phone', type: 'text', required: true },
      { name: 'position', label: 'Position', type: 'text', required: true },
      { name: 'department', label: 'Department', type: 'text', required: true }
    ],
    displayFields: [
      { key: 'employee_name', label: 'Name' },
      { key: 'email', label: 'Email' },
      { key: 'position', label: 'Position' },
      { key: 'department', label: 'Department' }
    ],
    idField: 'employee_id',
    api: {
      create: inventoryApi.createEmployee,
      delete: inventoryApi.deleteEmployee
    }
  },
  suppliers: {
    fields: [
      { name: 'supplier_id', label: 'Supplier ID', type: 'text', required: true },
      { name: 'supplier_name', label: 'Name', type: 'text', required: true },
      { name: 'email', label: 'Email', type: 'email', required: true },
      { name: 'phone', label: 'Phone', type: 'text', required: true },
      { name: 'address', label: 'Address', type: 'textarea', required: true }
    ],
    displayFields: [
      { key: 'supplier_name', label: 'Name' },
      { key: 'email', label: 'Email' },
      { key: 'phone', label: 'Phone' },
      { key: 'address', label: 'Address' }
    ],
    idField: 'supplier_id',
    api: {
      create: inventoryApi.createSupplier,
      delete: inventoryApi.deleteSupplier
    }
  },
  warehouses: {
    fields: [
      { name: 'warehouse_id', label: 'Warehouse ID', type: 'text', required: true },
      { name: 'warehouse_name', label: 'Name', type: 'text', required: true },
      { name: 'location', label: 'Location', type: 'text', required: true },
      { name: 'capacity', label: 'Capacity', type: 'number', required: true }
    ],
    displayFields: [
      { key: 'warehouse_name', label: 'Name' },
      { key: 'location', label: 'Location' },
      { key: 'capacity', label: 'Capacity', type: 'number' }
    ],
    idField: 'warehouse_id',
    api: {
      create: inventoryApi.createWarehouse,
      delete: inventoryApi.deleteWarehouse
    }
  },
  stocks: {
    fields: [
      { name: 'stock_id', label: 'Stock ID', type: 'text', required: true },
      { name: 'product_id', label: 'Product ID', type: 'text', required: true },
      { name: 'quantity', label: 'Quantity', type: 'number', required: true },
      { name: 'warehouse_id', label: 'Warehouse ID', type: 'text', required: true }
    ],
    displayFields: [
      { key: 'stock_id', label: 'Stock ID' },
      { key: 'product_id', label: 'Product ID' },
      { key: 'quantity', label: 'Quantity', type: 'number' },
      { key: 'warehouse_id', label: 'Warehouse ID' }
    ],
    idField: 'stock_id',
    api: {
      create: inventoryApi.createStock,
      delete: inventoryApi.deleteStock
    }
  },
  supplies: {
    fields: [
      { name: 'supply_id', label: 'Supply ID', type: 'text', required: true },
      { name: 'supplier_id', label: 'Supplier ID', type: 'text', required: true },
      { name: 'product_id', label: 'Product ID', type: 'text', required: true },
      { name: 'quantity', label: 'Quantity', type: 'number', required: true }
    ],
    displayFields: [
      { key: 'supply_id', label: 'Supply ID' },
      { key: 'supplier_id', label: 'Supplier ID' },
      { key: 'product_id', label: 'Product ID' },
      { key: 'quantity', label: 'Quantity', type: 'number' }
    ],
    idField: 'supply_id',
    api: {
      create: inventoryApi.createSupply,
      delete: inventoryApi.deleteSupply
    }
  }
};

const InventoryDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState('products');
  const [entities, setEntities] = useState(sampleData);
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState<NotificationState>({
    show: false,
    message: '',
    type: 'info'
  });

  const showNotification = (message: string, type: 'success' | 'error' | 'info' = 'info') => {
    setNotification({ show: true, message, type });
  };

  const hideNotification = () => {
    setNotification(prev => ({ ...prev, show: false }));
  };

  const handleCreate = async (data: Record<string, any>) => {
    setLoading(true);
    try {
      const config = entityConfigs[activeTab as keyof typeof entityConfigs];
      await config.api.create(data, false);
      
      // Add to local state
      setEntities(prev => ({
        ...prev,
        [activeTab]: [...prev[activeTab as keyof typeof prev], data]
      }));
      
      showNotification(`${activeTab.slice(0, -1)} created successfully!`, 'success');
    } catch (error) {
      console.error(`Failed to create ${activeTab.slice(0, -1)}:`, error);
      showNotification(`Failed to create ${activeTab.slice(0, -1)}. Please try again.`, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: string) => {
    setLoading(true);
    try {
      const config = entityConfigs[activeTab as keyof typeof entityConfigs];
      await config.api.delete(id);
      
      // Remove from local state
      setEntities(prev => ({
        ...prev,
        [activeTab]: prev[activeTab as keyof typeof prev].filter((item: any) => item[config.idField] !== id)
      }));
      
      showNotification(`${activeTab.slice(0, -1)} deleted successfully!`, 'success');
    } catch (error) {
      console.error(`Failed to delete ${activeTab.slice(0, -1)}:`, error);
      showNotification(`Failed to delete ${activeTab.slice(0, -1)}. Please try again.`, 'error');
    } finally {
      setLoading(false);
    }
  };

  const currentConfig = entityConfigs[activeTab as keyof typeof entityConfigs];
  const currentEntities = entities[activeTab as keyof typeof entities] || [];

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Inventory Management System</h1>
          <p className="mt-2 text-gray-600">
            Manage your complete inventory ecosystem with real-time updates
          </p>
        </div>

        {/* Tabs */}
        <div className="mb-8">
          <EntityTabs
            tabs={tabs}
            activeTab={activeTab}
            onTabChange={setActiveTab}
          />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Form */}
          <EntityForm
            fields={currentConfig.fields}
            onSubmit={handleCreate}
            loading={loading}
            title={`Add New ${activeTab.slice(0, -1)}`}
            submitLabel={`Add ${activeTab.slice(0, -1)}`}
          />

          {/* List */}
          <EntityList
            items={currentEntities}
            onDelete={handleDelete}
            loading={loading}
            title={`${activeTab.charAt(0).toUpperCase() + activeTab.slice(1)}`}
            idField={currentConfig.idField}
            displayFields={currentConfig.displayFields}
            emptyMessage={`No ${activeTab} found. Add your first ${activeTab.slice(0, -1)} above.`}
          />
        </div>

        {/* API Status */}
        <div className="mt-8 bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">API Information</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
            <div>
              <span className="font-medium text-gray-700">Base URL:</span>
              <span className="ml-2 text-gray-600">http://localhost:8081</span>
            </div>
            <div>
              <span className="font-medium text-gray-700">API Version:</span>
              <span className="ml-2 text-gray-600">v1</span>
            </div>
            <div>
              <span className="font-medium text-gray-700">Status:</span>
              <span className="ml-2 text-green-600">Connected</span>
            </div>
          </div>
        </div>
      </div>

      {/* Notification */}
      {notification.show && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={hideNotification}
        />
      )}

      {/* Debug Info */}
      <DebugInfo />
    </div>
  );
};

export default InventoryDashboard; 