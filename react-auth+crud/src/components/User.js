import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from 'react-router-dom';
import UserService from "../services/UserService.js";

const User = props => {
    const { id } = useParams();
    let navigate = useNavigate();

    const initialUserState = {
        id: id,
        email: "",
        password: "",
    };
    const [currentUser, setCurrentUser] = useState(initialUserState);
    const [message, setMessage] = useState("");

    const getUser = id => {
        UserService.get(id)
            .then(response => {
                setCurrentUser({ ...currentUser, ['email']:response.data.email  });
                console.log(response.data);
            })
            .catch(e => {
                console.log(e);
            });
    };

    useEffect(() => {
        if (id)
            getUser(id);
    }, [id]);

    const handleInputChange = event => {
        const { name, value } = event.target;
        setCurrentUser({ ...currentUser, [name]: value });
    };

    // const updatePublished = status => {
    //     var data = {
    //         id: currentUser.id,
    //         title: currentUser.title,
    //         description: currentUser.description,
    //         published: status
    //     };

    //     UserService.update(currentUser.id, data)
    //         .then(response => {
    //             setCurrentUser({ ...currentUser, published: status });
    //             console.log(response.data);
    //         })
    //         .catch(e => {
    //             console.log(e);
    //         });
    // };

    const updateUser = () => {
        UserService.update(currentUser.id, currentUser)
            .then(response => {
                console.log(response.data);
                setMessage("The User was updated successfully!");
            })
            .catch(e => {
                console.log(e);
            });
    };

    const deleteUser = () => {
        UserService.remove(currentUser.id)
            .then(response => {
                console.log(response.data);
                navigate("/users");
            })
            .catch(e => {
                console.log(e);
            });
    };

    return (
        <div>
            {currentUser ? (
                <div className="edit-form">
                    <h4>User</h4>
                    <form>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input
                                type="email"
                                className="form-control"
                                id="email"
                                name="email"
                                value={currentUser.email}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <input
                                type="password"
                                className="form-control"
                                id="password"
                                name="password"
                                value={currentUser.password}
                                onChange={handleInputChange}
                            />
                        </div>

                    </form>


                    <button className="btn btn-sm btn-danger " onClick={deleteUser}>
                        Delete
                    </button>

                    <button
                        type="submit"
                        className="btn btn-sm btn-success"
                        onClick={updateUser}
                    >
                        Update
                    </button>
                    <p>{message}</p>
                </div>
            ) : (
                <div>
                    <br />
                    <p>Please click on a User...</p>
                </div>
            )}
        </div>
    );
};

export default User;
