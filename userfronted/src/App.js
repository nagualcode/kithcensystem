import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Form, Button, Table, Alert } from 'react-bootstrap';

function App() {
  const [customerName, setCustomerName] = useState('');
  const [customerEmail, setCustomerEmail] = useState('');
  const [menuItems, setMenuItems] = useState([]);
  const [selectedPlates, setSelectedPlates] = useState([]);
  const [orders, setOrders] = useState([]);
  const [status, setStatus] = useState('');
  const [loggedIn, setLoggedIn] = useState(false);
  const [error, setError] = useState('');

  // Fetch the menu items from the menuservice API
  useEffect(() => {
    axios.get('http://localhost:8084/plates')
      .then(response => {
        setMenuItems(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the menu!', error);
      });
  }, []);

  // Fetch the list of orders based on logged-in customer
  const fetchOrders = () => {
    axios.get(`http://localhost:8085/orders?customerEmail=${customerEmail}`)
      .then(response => {
        setOrders(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching orders!', error);
      });
  };

  // Handle user login
  const handleLogin = (e) => {
    e.preventDefault();
    if (customerEmail) {
      setLoggedIn(true);
      fetchOrders();  // Fetch orders as soon as user logs in
    } else {
      setError('Please enter a valid email to log in.');
    }
  };

  // Handle order creation
  const createOrder = (e) => {
    e.preventDefault();

    if (selectedPlates.length === 0) {
      setError('Please select at least one plate.');
      return;
    }

    const orderData = {
      customerName,
      customerEmail,
      status: 'Pending',
      orderItems: selectedPlates.map(plate => ({
        plateDescription: plate.plateDescription,
        platePrice: plate.platePrice
      }))
    };

    axios.post('http://localhost:8085/orders', orderData)
      .then(() => {
        setStatus('Order created successfully!');
        setSelectedPlates([]);
        fetchOrders(); // Fetch updated orders list after creating an order
      })
      .catch(error => {
        console.error('There was an error creating the order!', error);
        setError('Error creating order.');
      });
  };

  // Handle plate selection
  const handlePlateSelection = (plate) => {
    if (!selectedPlates.includes(plate)) {
      setSelectedPlates([...selectedPlates, plate]);
    }
  };

  // Render the login form or the main interface based on login state
  return (
    <Container>
      {!loggedIn ? (
        <Form onSubmit={handleLogin}>
          <h2>Login</h2>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form.Group controlId="customerEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              type="email"
              placeholder="Enter email"
              value={customerEmail}
              onChange={(e) => setCustomerEmail(e.target.value)}
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Log In
          </Button>
        </Form>
      ) : (
        <div>
          <h2>Create an Order</h2>
          {status && <Alert variant="success">{status}</Alert>}
          <Form onSubmit={createOrder}>
            <Form.Group controlId="customerName">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter your name"
                value={customerName}
                onChange={(e) => setCustomerName(e.target.value)}
              />
            </Form.Group>

            <Form.Group controlId="menuItems">
              <Form.Label>Select Plates</Form.Label>
              <Form.Control as="select" onChange={(e) => handlePlateSelection(menuItems[e.target.selectedIndex])}>
                <option>Select a plate</option>
                {menuItems.map((plate, index) => (
                  <option key={index} value={plate.plateDescription}>
                    {plate.plateDescription} - ${plate.platePrice}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>

            <Button variant="success" type="submit">
              Create Order
            </Button>
          </Form>

          <h2 className="mt-5">My Orders</h2>
          {orders.length === 0 ? (
            <Alert variant="info">No orders found for {customerEmail}</Alert>
          ) : (
            <Table striped bordered hover className="mt-3">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Plates</th>
                  <th>Total Price</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((order) => (
                  <tr key={order.id}>
                    <td>{order.id}</td>
                    <td>
                      {order.orderItems.map((item, index) => (
                        <div key={index}>
                          {item.plateDescription} - ${item.platePrice}
                        </div>
                      ))}
                    </td>
                    <td>${order.totalPrice}</td>
                    <td>{order.status}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </div>
      )}
    </Container>
  );
}

export default App;
