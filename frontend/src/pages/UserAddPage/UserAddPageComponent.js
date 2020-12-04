import React, {useState} from 'react';
import {Button, Checkbox, Form, Input, Popconfirm, Spin} from 'antd'
import AppLayout from '../../components/AppLayout'
import {checkEmailAvailabilityRequest, checkUsernameAvailabilityRequest} from '../../utils/api'
import {useTranslation} from 'react-i18next';
import allroles from '../../utils/allroles'
import 'antd/dist/antd.css'
import './UserAddPage.css'

const {ADMINISTRATOR, MANAGER, CLIENT} = allroles;

const UserAddPage = (props) => {
    const [visible, setVisible] = useState(false);
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    const accessLevels = props.accessLevels;

    const onFinish = (values) => {
        const payload = Object.assign({}, values);
        delete payload.confirmPassword;
        props.onSubmit(payload);
    }

    const validateUsernameAvailability = async (rule, value) => {
        if (value) {
            const result = await checkUsernameAvailabilityRequest(value);
            if (result.data.available) return Promise.resolve('');
            else return Promise.reject(t('validation.usernameTaken'));
        }
    }

    const validateEmailAvailability = async (rule, value) => {
        if (value) {
            const result = await checkEmailAvailabilityRequest(value);
            if (result.data.available) return Promise.resolve('');
            else return Promise.reject(t('validation.emailTaken'));
        }
    }

    const showPopconfirm = () => {
        setVisible(true);
    }

    const handleCancel = () => {
        setVisible(false);
    }

    const handleOk = () => {
        setVisible(false);
        form.submit();
    }

    return (
        <AppLayout>
            {accessLevels ? (
                <div className="user-add-page-wrapper">
                    <h1 style={{fontWeight: "bold"}}> {t('pageName.userAdd')} </h1>
                    <Form
                        form={form}
                        layout="vertical"
                        name="user_add_form"
                        className="user-add-form"
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
                                        } else return Promise.reject(t('validation.passwordMismatch'))
                                    }
                                })
                            ]}
                        >
                            <Input.Password/>
                        </Form.Item>

                        <Form.Item
                            label={t('userLabels.roles')}
                            name="accessLevelIds"
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                }
                            ]}
                        >
                            <Checkbox.Group>
                                {accessLevels.map(accessLevel => {
                                    let text;

                                    if (accessLevel.name === ADMINISTRATOR)
                                        text = t('role.admin');
                                    else if (accessLevel.name === MANAGER)
                                        text = t('role.man');
                                    else if (accessLevel.name === CLIENT)
                                        text = t('role.client')

                                    return (
                                        <Checkbox key={accessLevel.id} value={accessLevel.id}> {text} </Checkbox>
                                    );
                                })}
                            </Checkbox.Group>
                        </Form.Item>

                        <Form.Item style={{marginBottom: '0'}}>
                            <Popconfirm
                                title={t('text.areYouSure')}
                                visible={visible}
                                onConfirm={handleOk}
                                onCancel={handleCancel}
                                okText={t('text.yes')}
                                cancelText={t('text.no')}
                            >
                                <Button type="primary" className="user-add-form-button" loading={isSubmitting}
                                        onClick={showPopconfirm}> {t('text.add')} </Button>
                            </Popconfirm>
                        </Form.Item>
                    </Form>
                </div>
            ) : (
                <Spin size="large"/>
            )}
        </AppLayout>
    );
}

export default UserAddPage;