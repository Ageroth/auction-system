import React from 'react';
import {Button, Form, Input} from 'antd'
import {useTranslation} from 'react-i18next';
import AppLayout from '../../components/AppLayout'
import 'antd/dist/antd.css'
import './PasswordResetPage.css'

const PasswordResetPage = props => {
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;

    const onFinish = (values) => {
        const payload = Object.assign({}, values);
        delete payload.confirmNewPassword;
        props.onSubmit(payload);
    };

    return (
        <AppLayout>
            <div className="password-reset-page-wrapper">
                <h1 style={{fontWeight: "bold"}}> {t('pageName.passwordReset')} </h1>
                {props.passwordResetCode ? (
                    <Form
                        form={form}
                        layout="vertical"
                        name="password_reset_form"
                        className="password-reset-form"
                        onFinish={onFinish}
                        scrollToFirstError
                    >
                        <Form.Item
                            label={t('userLabels.newPassword')}
                            name="newPassword"
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
                            label={t('userLabels.newPasswordConfirmation')}
                            name="confirmNewPassword"
                            dependencies={['newPassword']}
                            hasFeedback
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                },
                                ({getFieldValue}) => ({
                                    validator(rule, value) {
                                        if (!value || getFieldValue('newPassword') === value) return Promise.resolve();
                                        else return Promise.reject(t('validation.passwordMismatch'));
                                    }
                                })
                            ]}
                        >
                            <Input.Password/>
                        </Form.Item>

                        <Form.Item>
                            <Button type="primary" htmlType="submit" className="password-reset-form-button"
                                    loading={isSubmitting}>
                                {t('text.changePassword')}
                            </Button>
                        </Form.Item>
                    </Form>
                ) : (
                    <>
                        {props.emailSent ? (
                            <div className="activation-box">
                                <h3> {t('message.title.passwordResetEmailSent')} </h3>
                                <p> {t('message.content.passwordResetEmailSent')} </p>
                            </div>
                        ) : (
                            <Form
                                form={form}
                                layout="vertical"
                                name="password_reset_form"
                                className="password-reset-form"
                                onFinish={onFinish}
                                scrollToFirstError
                            >
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
                                        }
                                    ]}
                                >
                                    <Input/>
                                </Form.Item>

                                <Form.Item>
                                    <Button type="primary" htmlType="submit" className="password-reset-form-button"
                                            loading={isSubmitting}>
                                        {t('text.sendPasswordResetEmail')}
                                    </Button>
                                </Form.Item>
                            </Form>
                        )}
                    </>
                )}
            </div>
        </AppLayout>
    );
}

export default PasswordResetPage;