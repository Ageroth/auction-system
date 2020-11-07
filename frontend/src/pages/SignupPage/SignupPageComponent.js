import React, {useState} from 'react'
import {Button, Form, Input} from 'antd'
import {checkUsernameAvailabilityRequest,checkEmailAvailabilityRequest} from '../../utils/api'
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css'
import './SignupPage.css'


const SignupPage = (props) => {
    const [form] = Form.useForm();
    const [usernameValidationStatus, setUsernameValidationStatus] = useState('');
    const [usernameErrorMsg, setUsernameErrorMsg] = useState(null);
    const [emailValidationStatus, setEmailValidationStatus] = useState('');
    const [emailErrorMsg, setEmailErrorMsg] = useState(null);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const {getFieldError} = form;
    const {t} = useTranslation();

    const onFinish = values => {
        const payload = Object.assign({}, values);
        delete payload.confirmPassword;
        props.onSubmit(payload)
            .then(() => {
                console.log('Success');
            }).catch(error => {
                console.log(error);
        })
    }

    const validateUsernameAvailability = () => {
        if (getFieldError('username').length === 0 && username.length > 0) {
            checkUsernameAvailabilityRequest(username)
                .then(response => {
                    if (response.data.available) {
                        setUsernameValidationStatus('success');
                        setUsernameErrorMsg('')
                    } else {
                        setUsernameValidationStatus('error');
                        setUsernameErrorMsg(t('validation.usernameTaken'));
                    }
                })
        }
    }

    const validateEmailAvailability = () => {
        if (getFieldError('email').length === 0 && email.length > 0) {
            checkEmailAvailabilityRequest(email)
                .then(response => {
                    if (response.data.available) {
                        setEmailValidationStatus('success')
                        setEmailErrorMsg('')
                    } else {
                        setEmailValidationStatus('error')
                        setEmailErrorMsg(t('validation.emailTaken'))
                    }
                })
        }
    }

    return (
        <Form
            form={form}
            layout="vertical"
            name="registration_form"
            className="registration-form"
            onFinish={onFinish}
            scrollToFirstError
        >
            <Form.Item
                label={t('user-labels.username')}
                validateStatus={usernameValidationStatus}
                help={usernameErrorMsg}
                name="username"
                rules={[
                    {
                        required: true
                    },
                    {
                        validator: (rule, value) => {
                            if (value) {
                                setUsernameValidationStatus('success')
                                setUsernameErrorMsg('')
                                return Promise.resolve()
                            } else {
                                setUsernameValidationStatus('error')
                                setUsernameErrorMsg(t('validation.required'))
                                return Promise.reject('')
                            }
                        }
                    }
                ]}
            >
                <Input
                    value={username}
                    onChange={event => setUsername(event.target.value)}
                    onBlur={validateUsernameAvailability}
                />
            </Form.Item>

            <Form.Item
                label={t('user-labels.firstName')}
                name="firstName"
                rules={[
                    {
                        required: true,
                        message: t('validation.required')
                    }
                ]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                label={t('user-labels.lastName')}
                name="lastName"
                rules={[
                    {
                        required: true,
                        message: t('validation.required')
                    }
                ]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                label={t('user-labels.phoneNumber')}
                name="phoneNumber"
                rules={[
                    {
                        required: true,
                        message: t('validation.required')
                    }
                ]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                label={t('user-labels.email')}
                validateStatus={emailValidationStatus}
                help={emailErrorMsg}
                name="email"
                rules={[
                    {
                        required: true,
                    },
                    {
                        validator: (rule, value) => {
                            if (value) {
                                setEmailValidationStatus("success")
                                setEmailErrorMsg('')
                                return Promise.resolve()
                            } else {
                                setEmailValidationStatus("error")
                                setEmailErrorMsg(t('validation.required'))
                                return Promise.reject('')
                            }
                        }
                    }
                ]}
            >
                <Input
                    value={email}
                    onChange={event => setEmail(event.target.value)}
                    onBlur={validateEmailAvailability}/>
            </Form.Item>

            <Form.Item
                label={t('user-labels.password')}
                name="password"
                hasFeedback
                rules={[
                    {
                        required: true,
                        message: t('validation.required')
                    }
                ]}
            >
                <Input.Password/>
            </Form.Item>

            <Form.Item
                label={t('user-labels.passwordConfirmation')}
                name="confirmPassword"
                dependencies={['password']}
                hasFeedback
                rules={[
                    {
                        required: true,
                        message: t('validation.required')
                    },
                    ({getFieldValue}) => ({
                        validator(rule, value) {
                            if (!value || getFieldValue('password') === value) {
                                return Promise.resolve()
                            } else return Promise.reject(t('validation.passwordMistmatch'))
                        }
                    })
                ]}
            >
                <Input.Password/>
            </Form.Item>

            <Form.Item>
                <Button type="primary" htmlType="submit" className="registration-form-button">
                    {t('text.signUp')}
                </Button>
                <p className="login-link"> {t('text.alreadyHaveAnAccount')} <a href="/login"> {t('text.logIn')} </a></p> 
            </Form.Item>
        </Form>
    )
}

export default SignupPage;