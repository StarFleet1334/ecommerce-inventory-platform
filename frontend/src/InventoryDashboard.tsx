import React, { useState } from 'react';

interface Product {
  product_name: string;
  sku: string;
  product_id: string;
  product_price: number;
  product_description: string;
}

const initialProducts: Product[] = [
  {
    product_name: 'Sample Product',
    sku: 'SKU123',
    product_id: '1',
    product_price: 19.99,
    product_description: 'A sample product.'
  }
];

const InventoryDashboard: React.FC = () => {
  const [products, setProducts] = useState<Product[]>(initialProducts);
  const [form, setForm] = useState<Product>({
    product_name: '',
    sku: '',
    product_id: '',
    product_price: 0,
    product_description: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Handle form input changes
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: name === 'product_price' ? parseFloat(value) : value
    }));
  };

  // Handle product creation
  const handleAddProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const response = await fetch('/api/inventory/api/v1/product?initialLoad=false', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      if (!response.ok) throw new Error('Failed to add product');
      setProducts(prev => [...prev, form]);
      setForm({ product_name: '', sku: '', product_id: '', product_price: 0, product_description: '' });
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // Handle product deletion
  const handleDelete = async (id: string) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`/api/inventory/api/v1/product/${id}`, {
        method: 'DELETE'
      });
      if (!response.ok) throw new Error('Failed to delete product');
      setProducts(prev => prev.filter(p => p.product_id !== id));
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 600, margin: '2rem auto', padding: '2rem', background: '#fff', borderRadius: 8, boxShadow: '0 2px 8px #eee' }}>
      <h2>Inventory Dashboard</h2>
      <form onSubmit={handleAddProduct} style={{ marginBottom: '2rem' }}>
        <input name="product_name" placeholder="Product Name" value={form.product_name} onChange={handleChange} required style={{ width: '100%', marginBottom: 8 }} />
        <input name="sku" placeholder="SKU" value={form.sku} onChange={handleChange} required style={{ width: '100%', marginBottom: 8 }} />
        <input name="product_id" placeholder="Product ID" value={form.product_id} onChange={handleChange} required style={{ width: '100%', marginBottom: 8 }} />
        <input name="product_price" type="number" step="0.01" placeholder="Price" value={form.product_price} onChange={handleChange} required style={{ width: '100%', marginBottom: 8 }} />
        <textarea name="product_description" placeholder="Description" value={form.product_description} onChange={handleChange} required style={{ width: '100%', marginBottom: 8 }} />
        <button type="submit" disabled={loading} style={{ width: '100%' }}>Add Product</button>
      </form>
      {error && <div style={{ color: 'red', marginBottom: 8 }}>{error}</div>}
      <h3>Products</h3>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {products.map(product => (
          <li key={product.product_id} style={{ borderBottom: '1px solid #eee', padding: '8px 0' }}>
            <strong>{product.product_name}</strong> (SKU: {product.sku})<br />
            Price: ${product.product_price} <br />
            {product.product_description}<br />
            <button onClick={() => handleDelete(product.product_id)} disabled={loading} style={{ marginTop: 4 }}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default InventoryDashboard; 