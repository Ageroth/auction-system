import React from 'react'
import {Button, Form, Input} from 'antd'
import {checkUsernameAvailabilityRequest,checkEmailAvailabilityRequest} from '../../utils/api'
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css'
import './SignupPage.css'


const SignupPage = (props) => {
    const [form] = Form.useForm();
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
                name="username"
                rules={[
                    {
                        required: true,
                        message: t('validation.required')
                    },
                    {
                        validator: validateUsernameAvailability
                    }
                ]}
            >
                <Input/>
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
                name="email"
                rules={[
                    {
                        required: true,
                        message: t('validation.required')
                    },
                    {
                        validator: validateEmailAvailability
                    }
                ]}
            >
                <Input/>
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