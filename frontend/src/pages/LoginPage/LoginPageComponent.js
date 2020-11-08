import React from 'react';
import {Button, Form, Input} from 'antd';
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css'
import './LoginPage.css'

const LoginPage = props => {
    const {t} = useTranslation();
    const onFinish = values => {
        const payload = Object.assign({}, values);
        props.onSubmit(payload);
    }

    return (
        <Form
            layout={'vertical'}
            name="login_form"
            className="login-form"
            onFinish={onFinish}>

            <Form.Item
                label={t('user-labels.username')}
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
                label={t('user-labels.password')}
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
                <Button type="primary" htmlType="submit" className="login-form-button">
                    {t('text.logIn')}
                </Button>
                <p> {t('text.noAccount')} <a href="/signup">{t('text.signUp')} </a></p>
                <p><a href="../registration"> {t('text.forgotPassword')} </a></p>
            </Form.Item>
        </Form>
    );
}

export default LoginPage;