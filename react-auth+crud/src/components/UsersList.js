import React, { useState, useEffect } from "react";
import UserService from "../services/UserService.js";
import { Link } from "react-router-dom";
import AuthService from "../services/AuthService.js";

const UsersList = () => {
    const AuthenticatedUser = AuthService.getCurrentUser();
    const [users, setUsers] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const [currentIndex, setCurrentIndex] = useState(-1);
    const [searchId, setSearchId] = useState("");
    const [selectedUsers, setSelectedUsers] = useState([]);

    useEffect(() => {
        retrieveUsers();
    }, );
    const toggleSelectUser = (userId) => {
        if (selectedUsers.includes(userId)) {
            setSelectedUsers(selectedUsers.filter((id) => id !== userId));
        } else {
            setSelectedUsers([...selectedUsers, userId]);
        }
    };


    const deleteSelectedUsers = () => {
        UserService.removeMultiple(selectedUsers)
            .then(response => {
                console.log(response.data);
                setSelectedUsers([]);
                refreshList();
            })
            .catch(e => {
                console.log(e);
            });
    }

    const onChangeSearchId = e => {
        const searchId = e.target.value;
        setSearchId(searchId);
        if (searchId === "") {
            refreshList();
        }

    };

    const retrieveUsers = () => {
        UserService.getAll()
            .then(response => {
                setUsers(response.data.filter((user) => user.id !== AuthenticatedUser.id));
                
            })
            .catch(e => {
                console.log(e);
            });
    };

    const refreshList = () => {
        retrieveUsers();
        setCurrentUser(null);
        setCurrentIndex(-1);
    };

    const setActiveUser = (user, index) => {
        setCurrentUser(user);
        setCurrentIndex(index);
    };



    const findById = () => {
        UserService.get(searchId)
            .then(response => {
                const updatedUsers = searchId ? [response.data] : users;
                setUsers(updatedUsers);
            })
            .catch(e => {
                console.log(e);
            });
    };
    

    return (
        <div className="list row">
            <div className="col-md-8">
                <div className="input-group mb-3">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Search by id"
                        value={searchId}
                        onChange={onChangeSearchId}
                    />
                    <div className="input-group-append">
                        <button
                            className="btn btn-outline-secondary"
                            type="button"
                            onClick={findById}
                        >
                            Search
                        </button>
                    </div>
                </div>
            </div>
            <div className="col-md-6">
                <h4>Users List</h4>

                <ul className="list-group">
                    {users &&
                        users.map((user, index) => (
                            <li
                                className={
                                    "list-group-item " + (index === currentIndex ? "active" : "") +
                                    (selectedUsers.includes(user.id) ? " selected" : "")
                                }
                                onClick={() => {
                                    setActiveUser(user, index);
                                    toggleSelectUser(user.id);
                                }}
                                key={index}
                            >
                                <input
                                    type="checkbox"
                                    checked={selectedUsers.includes(user.id)}
                                    onChange={() => toggleSelectUser(user.id)}
                                />

                                {user.username}
                            </li>
                        ))}
                </ul>

                <button
                    className="m-3 btn btn-sm btn-danger"
                    onClick={deleteSelectedUsers}
                    disabled={selectedUsers.length === 0}
                >
                    Delete selected
                </button>
            </div>
            <div className="col-md-6">
                {currentUser ? (
                    <div>
                        <h4>User</h4>
                        <div>
                            <label>
                                <strong>Username:</strong>
                            </label>{" "}
                            {currentUser.username}
                        </div>
                        <div>
                            <label>
                                <strong>ID:</strong>
                            </label>{" "}
                            {currentUser.id}
                        </div>
                        <div>
                            <label>
                                <strong>email:</strong>
                            </label>{" "}
                            {currentUser.email}
                        </div>

                        <Link
                            to={"/users/" + currentUser.id}
                            className="btn btn-sm btn-warning"
                        >
                            Edit
                        </Link>
                    </div>
                ) : (
                    <div>
                        <br />
                        <p>Please click on a User...</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default UsersList;
