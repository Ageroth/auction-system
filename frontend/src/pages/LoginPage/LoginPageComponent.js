import React from 'react';
import {Form, Formik} from 'formik';
import * as Yup from 'yup';
import LoginInput from "./LoginInput.js";

const LoginPage = props => {
    const {text} = props;
    return (
        <>
            <h1>{text}</h1>
            <Formik
                initialValues={{
                    username: '',
                    password: '',
                }}
                validationSchema={Yup.object({
                    username: Yup.string()
                        .max(20, 'Must be 15 characters or less')
                        .required('Required'),
                    password: Yup.string()
                        .max(20, 'Must be 20 characters or less')
                        .required('Required'),
                })}
                onSubmit={(values, {setSubmitting}) => {
                    setTimeout(() => {
                        alert(JSON.stringify(values, null, 2));
                        setSubmitting(false);
                    }, 400);
                }}
            >
                <Form>
                    <LoginInput
                        label="Username"
                        name="username"
                        type="text"
                        // placeholder="Jane"
                    />
                    <LoginInput
                        label="Password"
                        name="password"
                        type="text"
                        // placeholder="Doe"
                    />

                    <button type="submit">Submit</button>
                </Form>
            </Formik>
        </>
    );
};

export default LoginPage;