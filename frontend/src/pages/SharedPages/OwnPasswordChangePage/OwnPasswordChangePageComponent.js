import React from 'react';
import {Button, Form, Input} from 'antd'
import {useTranslation} from 'react-i18next';
import AppLayout from '../../../components/AppLayout'
import 'antd/dist/antd.css'
import './OwnPasswordChangePage.css'

const OwnPasswordChangePage = (props) => {
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
            <div className="own-password-change-page-wrapper">
                <h1 style={{fontWeight: "bold"}}> {t('pageName.passwordChange')} </h1>
                <Form
                    form={form}
                    layout="vertical"
                    name="own_password_change_form"
                    className="own-password-change-form"
                    onFinish={onFinish}
                    scrollToFirstError
                >
                    <Form.Item
                        label={t('userLabels.currentPassword')}
                        name="currentPassword"
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

                    <Form.Item style={{marginBottom: "0"}}>
                        <Button type="primary" htmlType="submit" className="own-password-change-form-button"
                                loading={isSubmitting}> {t('text.changePassword')} </Button>
                    </Form.Item>
                </Form>
            </div>
        </AppLayout>
    );
}

export default OwnPasswordChangePage;