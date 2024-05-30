import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = "http://localhost:8080/api/user";


const getAll = () => {
    return axios.get(API_URL + "/all", { headers: authHeader() });
};

const get = id => {
    return axios.get(API_URL + `/${id}`, { headers: authHeader() });
};

const create = data => {
    return axios.post(API_URL + "/add", data, { headers: authHeader() });
};

const update = (id, data) => {
    return axios.put(API_URL + `/update/${id}`, data, { headers: authHeader() });
};

const remove = id => {
    return axios.delete(API_URL + `/delete/${id}`, { headers: authHeader() });
};
const removeMultiple = (userIds) => {
    return axios.delete(API_URL + `/delete/userIds?ids=${userIds.join(',')}`, {
        headers: authHeader(),
        // params: { ids: userIds }
    }

    );
}

const UserService = {
    getAll,
    get,
    create,
    update,
    remove,
    removeMultiple,
};

export default UserService;