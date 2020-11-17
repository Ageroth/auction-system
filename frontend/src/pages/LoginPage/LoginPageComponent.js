import React from 'react';
import { Button, Form, Input } from 'antd';
import { Link } from 'react-router-dom';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next';
import AppLayout from '../../components/AppLayout'
import 'antd/dist/antd.css'
import './LoginPage.css'

const LoginPage = props => {
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    const onFinish = values => {
        const payload = Object.assign({}, values);
        props.onSubmit(payload);
    };

    return (
        <AppLayout>
            <Form
            layout={'vertical'}
            name="login_form"
            className="login-form"
            onFinish={onFinish}>
                <Form.Item
                    label={t('userLabels.username')}
                    name="username"
                    rules={[
                        {
                            required: true,
                            message: (t('validation.required'))
                        }
                    ]}
                >
                    <Input prefix={<UserOutlined  className="site-form-item-icon"/>} />
                </Form.Item>

                <Form.Item
                    label={t('userLabels.password')}
                    name="password"
                    rules={[
                        {
                            required: true,
                            message: (t('validation.required'))
                        }
                    ]}
                >
                    <Input.Password prefix={<LockOutlined className="site-form-item-icon"/>} />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" className="login-form-button" disabled={isSubmitting}>
                        {t('text.logIn')}
                    </Button>
                    <p className="sign-up"> 
                        {t('text.noAccount')} 
                        <Link style={{ color: "#1890ff" }} to={'/signup'}> {t('text.signUp')} </Link> 
                    </p>
                    <p className="forgot-password"> 
                        <Link style={{ color: "#1890ff" }} to={'/password_reset'}> {t('text.forgotPassword')} </Link> 
                    </p>
                </Form.Item>
            </Form>
        </AppLayout>
    );
}

export default LoginPage;