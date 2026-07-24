# ReactJS Hands-On Exercises Solutions

This document consolidates solutions across the 19 ReactJS hands-on exercises, covering foundational concepts, state management, forms, routing, API consumption, and unit testing.

## 1. Environment Setup & Basic JSX

**Create a React Application:**
```bash
npx create-react-app myfirstreact
cd myfirstreact
npm start
```

**`App.js` (Basic JSX & Styling):**
```jsx
import React from 'react';
import './App.css';

function App() {
  const office = {
    name: "Cognizant Technology Solutions",
    address: "123 Tech Park, IT Corridor",
    rent: 55000
  };

  const rentStyle = {
    color: office.rent < 60000 ? 'red' : 'green'
  };

  return (
    <div className="App">
      <h1>Welcome to the first session of React</h1>
      <h2>Office Space Rental Details</h2>
      <img src="https://via.placeholder.com/150" alt="Office Space" />
      <p>Name: {office.name}</p>
      <p>Address: {office.address}</p>
      <p>Rent: <span style={rentStyle}>${office.rent}</span></p>
    </div>
  );
}

export default App;
```

---

## 2. Components, Props & State (Class & Functional)

**`CountPeople.js` (State in Class Component):**
```jsx
import React, { Component } from 'react';

class CountPeople extends Component {
  constructor(props) {
    super(props);
    this.state = {
      entryCount: 0,
      exitCount: 0
    };
  }

  updateEntry = () => {
    this.setState({ entryCount: this.state.entryCount + 1 });
  };

  updateExit = () => {
    this.setState({ exitCount: this.state.exitCount + 1 });
  };

  render() {
    return (
      <div>
        <h2>Mall Counter</h2>
        <button onClick={this.updateEntry}>Login</button>
        <button onClick={this.updateExit}>Exit</button>
        <p>Entered: {this.state.entryCount}</p>
        <p>Exited: {this.state.exitCount}</p>
      </div>
    );
  }
}

export default CountPeople;
```

**`CalculateScore.js` (Props in Functional Component):**
```jsx
import React from 'react';
import '../Stylesheets/mystyle.css'; // Importing CSS module/file

const CalculateScore = ({ name, school, total, goal }) => {
  const average = total / goal;

  return (
    <div className="score-card">
      <h3>Student Score Details</h3>
      <p>Name: {name}</p>
      <p>School: {school}</p>
      <p>Average Score: {average.toFixed(2)}</p>
    </div>
  );
};

export default CalculateScore;
```

---

## 3. Form Handling & Validation

**`Register.js` (Controlled Components & Validation):**
```jsx
import React, { useState } from 'react';

const Register = () => {
  const [formData, setFormData] = useState({ name: '', email: '', password: '' });
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validate = () => {
    let tempErrors = {};
    if (formData.name.length < 5) tempErrors.name = "Name must be at least 5 characters.";
    if (!formData.email.includes('@') || !formData.email.includes('.')) tempErrors.email = "Invalid Email.";
    if (formData.password.length < 8) tempErrors.password = "Password must be at least 8 characters.";
    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      alert("Registration Successful!");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Name:</label>
        <input type="text" name="name" value={formData.name} onChange={handleChange} />
        {errors.name && <span style={{ color: 'red' }}>{errors.name}</span>}
      </div>
      <div>
        <label>Email:</label>
        <input type="text" name="email" value={formData.email} onChange={handleChange} />
        {errors.email && <span style={{ color: 'red' }}>{errors.email}</span>}
      </div>
      <div>
        <label>Password:</label>
        <input type="password" name="password" value={formData.password} onChange={handleChange} />
        {errors.password && <span style={{ color: 'red' }}>{errors.password}</span>}
      </div>
      <button type="submit">Register</button>
    </form>
  );
};

export default Register;
```

---

## 4. Lifecycle Hooks & Fetch API

**`GetUser.js` (Fetching Data from API):**
```jsx
import React, { Component } from 'react';

class GetUser extends Component {
  constructor(props) {
    super(props);
    this.state = { user: null };
  }

  componentDidMount() {
    fetch('https://api.randomuser.me/')
      .then(response => response.json())
      .then(data => {
        this.setState({ user: data.results[0] });
      })
      .catch(error => console.error("Error fetching data:", error));
  }

  render() {
    const { user } = this.state;
    if (!user) return <div>Loading...</div>;

    return (
      <div>
        <h2>User Profile</h2>
        <img src={user.picture.large} alt="Profile" />
        <p>Name: {user.name.title} {user.name.first} {user.name.last}</p>
      </div>
    );
  }
}

export default GetUser;
```

---

## 5. React Router (Routing & Navigation)

**`App.js` (Router Configuration):**
```jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Home from './Home';
import TrainersList from './TrainersList';
import TrainerDetail from './TrainerDetail';

function App() {
  return (
    <Router>
      <nav>
        <ul>
          <li><Link to="/">Home</Link></li>
          <li><Link to="/trainers">Trainers</Link></li>
        </ul>
      </nav>
      
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/trainers" element={<TrainersList />} />
        <Route path="/trainers/:id" element={<TrainerDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
```
*(Note: `useParams()` from `react-router-dom` is used in `TrainerDetail` to retrieve the `id` from the URL).*

---

## 6. React Context API (Theme Switching)

**`ThemeContext.js`:**
```jsx
import React from 'react';
const ThemeContext = React.createContext('light');
export default ThemeContext;
```

**`App.js` & `EmployeeCard.js` (Provider and Consumer):**
```jsx
// In App.js
import ThemeContext from './ThemeContext';

<ThemeContext.Provider value={this.state.theme}>
  <EmployeeList />
</ThemeContext.Provider>

// In EmployeeCard.js
import React, { useContext } from 'react';
import ThemeContext from './ThemeContext';

const EmployeeCard = () => {
  const theme = useContext(ThemeContext);
  return (
    <div className={`card ${theme}`}>
      <button className={`btn-${theme}`}>Action</button>
    </div>
  );
};
```

---

## 7. Unit Testing with Jest & Enzyme

**`GitClient.test.js` (Mocking Axios):**
```javascript
import GitClient from './GitClient';
import axios from 'axios';

jest.mock('axios');

describe("Git Client Tests", () => {
  it("should return repository names for techiesyed", async () => {
    const mockData = { data: [{ name: 'repo1' }, { name: 'repo2' }] };
    axios.get.mockResolvedValue(mockData);

    const client = new GitClient();
    const repos = await client.getRepositories('techiesyed');

    expect(repos).toEqual([{ name: 'repo1' }, { name: 'repo2' }]);
    expect(axios.get).toHaveBeenCalledWith('https://api.github.com/users/techiesyed/repos');
  });
});
```

**`CohortDetails.test.js` (Enzyme Component Testing):**
```javascript
import React from 'react';
import { shallow } from 'enzyme';
import CohortDetails from './CohortDetails';

describe("Cohort Details Component", () => {
  it("should create the component", () => {
    const wrapper = shallow(<CohortDetails cohort={{}} />);
    expect(wrapper.exists()).toBe(true);
  });

  it("should display cohort code in h3", () => {
    const mockCohort = { code: "CH-123", status: "ongoing" };
    const wrapper = shallow(<CohortDetails cohort={mockCohort} />);
    expect(wrapper.find('h3').text()).toBe("CH-123");
  });

  it("should always render same html (Snapshot)", () => {
    const mockCohort = { code: "CH-123", status: "ongoing" };
    const wrapper = shallow(<CohortDetails cohort={mockCohort} />);
    expect(wrapper).toMatchSnapshot();
  });
});
```
