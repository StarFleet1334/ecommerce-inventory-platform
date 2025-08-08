import React, { useState } from "react";
import type { ProductMessage } from "../types/inventory";

interface ProductFormProps {
  onSubmit: (product: ProductMessage) => Promise<void>;
  loading: boolean;
  initialData?: Partial<ProductMessage>;
}

const ProductForm: React.FC<ProductFormProps> = ({
  onSubmit,
  loading,
  initialData,
}) => {
  const [form, setForm] = useState<ProductMessage>({
    product_name: initialData?.product_name || "",
    sku: initialData?.sku || "",
    product_id: initialData?.product_id || "",
    product_price: initialData?.product_price || 0,
    product_description: initialData?.product_description || "",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "product_price" ? parseFloat(value) || 0 : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await onSubmit(form);
    // Reset form after successful submission
    setForm({
      product_name: "",
      sku: "",
      product_id: "",
      product_price: 0,
      product_description: "",
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label
          htmlFor="product_name"
          className="block text-sm font-medium text-gray-700"
        >
          Product Name
        </label>
        <input
          id="product_name"
          name="product_name"
          type="text"
          required
          value={form.product_name}
          onChange={handleChange}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
          placeholder="Enter product name"
        />
      </div>

      <div>
        <label
          htmlFor="sku"
          className="block text-sm font-medium text-gray-700"
        >
          SKU
        </label>
        <input
          id="sku"
          name="sku"
          type="text"
          required
          value={form.sku}
          onChange={handleChange}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
          placeholder="Enter SKU"
        />
      </div>

      <div>
        <label
          htmlFor="product_id"
          className="block text-sm font-medium text-gray-700"
        >
          Product ID
        </label>
        <input
          id="product_id"
          name="product_id"
          type="text"
          required
          value={form.product_id}
          onChange={handleChange}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
          placeholder="Enter product ID"
        />
      </div>

      <div>
        <label
          htmlFor="product_price"
          className="block text-sm font-medium text-gray-700"
        >
          Price
        </label>
        <input
          id="product_price"
          name="product_price"
          type="number"
          step="0.01"
          min="0"
          required
          value={form.product_price}
          onChange={handleChange}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
          placeholder="Enter price"
        />
      </div>

      <div>
        <label
          htmlFor="product_description"
          className="block text-sm font-medium text-gray-700"
        >
          Description
        </label>
        <textarea
          id="product_description"
          name="product_description"
          required
          value={form.product_description}
          onChange={handleChange}
          rows={3}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
          placeholder="Enter product description"
        />
      </div>

      <button
        type="submit"
        disabled={loading}
        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        {loading ? "Adding Product..." : "Add Product"}
      </button>
    </form>
  );
};

export default ProductForm;
