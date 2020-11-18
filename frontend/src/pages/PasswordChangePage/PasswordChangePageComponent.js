import React from 'react';
import { Button, Form, Input } from 'antd'
import { useTranslation } from 'react-i18next';
import AppLayout from '../../components/AppLayout'
import 'antd/dist/antd.css'

const PasswordChangePage = props => {
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    
    const onFinish = values => {
        const payload = Object.assign({}, values);
        delete payload.confirmNewPassword;
        props.onSubmit(payload);
    };

    return (
        <AppLayout>
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
                                else return Promise.reject(t('validation.passwordMistmatch'));
                            }
                        })
                    ]}
                >
                    <Input.Password/>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" className="password-reset-form-button" disabled={isSubmitting}>
                        {t('text.changePassword')}
                    </Button>
                </Form.Item>
            </Form> 
        </AppLayout>
    );
}

export default PasswordChangePage;