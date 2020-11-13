import React from 'react'
import {Button, Form, Input} from 'antd'
import {checkUsernameAvailabilityRequest, checkEmailAvailabilityRequest} from '../../utils/api'
import {toast} from 'react-toastify';
import {useTranslation} from 'react-i18next';
import AppLayout from '../../components/AppLayout/AppLayout'
import 'antd/dist/antd.css'
import './SignupPage.css'


const SignupPage = (props) => {
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    const onFinish = values => {
        const payload = Object.assign({}, values);
        delete payload.confirmPassword;
        
        props.onSubmit(payload)
            .then(() => {
                toast.success(t('message.content.activationEmailSent'), {
                    position: "bottom-right",
                    autoClose: false,
                    closeOnClick: true
                    });
            })
            .catch(e => {
                toast.error(e.response.data.message, {
                    position: "bottom-right",
                    autoClose: 3000,
                    closeOnClick: true
                    });
            })
    };

    const validateUsernameAvailability = async (rule, value) => {
            if(value) {
                const result = await checkUsernameAvailabilityRequest(value);
                if(result.data.available)  {
                    return Promise.resolve('');
                  } else {
                    return Promise.reject(t('validation.usernameTaken'));
                  }
            }
    }

    const validateEmailAvailability = async (rule, value) => {
        if(value) {
            const result = await checkEmailAvailabilityRequest(value);
            if(result.data.available)  {
                return Promise.resolve('');
              } else {
                return Promise.reject(t('validation.emailTaken'));
              }
        }
    }
           
    return (
        <AppLayout> 
            <Form
                form={form}
                layout="vertical"
                name="signup_form"
                className="signup-form"
                onFinish={onFinish}
                scrollToFirstError
            >
                <Form.Item
                    label={t('userLabels.username')}
                    name="username"
                    rules={[
                        {
                            required: true,
                            message: t('validation.required')
                        },
                        {
                            pattern: new RegExp("^[a-zA-Z0-9]*$"),
                            message: t('validation.regex.username')
                        },
                        {
                            max: 32,
                            message: t('validation.max32chars')
                        },
                        {
                            validator: validateUsernameAvailability
                        }
                    ]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    label={t('userLabels.firstName')}
                    name="firstName"
                    rules={[
                        {
                            required: true,
                            message: t('validation.required')
                        },
                        {
                            pattern: new RegExp("^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$"),
                            message: t('validation.regex.name')
                        },
                        {
                            max: 32,
                            message: t('validation.max32chars')
                        }
                    ]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    label={t('userLabels.lastName')}
                    name="lastName"
                    rules={[
                        {
                            required: true,
                            message: t('validation.required')
                        },
                        {
                            pattern: new RegExp("^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$"),
                            message: t('validation.regex.name')
                        },
                        {
                            max: 32,
                            message: t('validation.max32chars')
                        }
                    ]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    label={t('userLabels.phoneNumber')}
                    name="phoneNumber"
                    rules={[
                        {
                            required: true,
                            message: t('validation.required')
                        },
                        {
                            pattern: new RegExp("^[0-9]{9,10}$"),
                            message: t('validation.regex.phoneNumber')
                        }
                    ]} 
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    label={t('userLabels.email')}
                    name="email"
                    rules={[
                        {
                            required: true,
                            message: t('validation.required')
                        },
                        {
                            type: "email",
                            message: t('validation.regex.email')
                        },
                        {
                            max: 32,
                            message: t('validation.max32chars')
                        },
                        {
                            validator: validateEmailAvailability
                        }
                    ]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    label={t('userLabels.password')}
                    name="password"
                    hasFeedback
                    rules={[
                        {
                            required: true,
                            message: t('validation.required')
                        },
                        {
                            pattern: new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$"),
                            message: t('validation.regex.password')
                        },
                        {
                            max: 64,
                            message: t('validation.max64chars')
                        }
                    ]}
                >
                    <Input.Password/>
                </Form.Item>

                <Form.Item
                    label={t('userLabels.passwordConfirmation')}
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
                    <Button type="primary" htmlType="submit" className="signup-form-button" disabled={isSubmitting}>
                        {t('text.signUp')}
                    </Button>
                    <p className="log-in"> {t('text.alreadyHaveAnAccount')} <a href="/login"> {t('text.logIn')} </a></p> 
                </Form.Item>
            </Form>
        </AppLayout>
    )
}

export default SignupPage;