import React from 'react';
import {Form, Formik} from 'formik';
import * as Yup from 'yup';
import LoginInput from "./LoginInput.js";
import {useTranslation} from 'react-i18next';

const LoginPage = props => {
    const {t} = useTranslation();
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
                        .min(4, 'Must be 4 characters or more')
                        .required(t('validation.required')),
                    password: Yup.string()
                        .required(t('validation.required'))
                        .matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}/, t('validation.passwordRegex'))
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
                        label={t('username')}
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