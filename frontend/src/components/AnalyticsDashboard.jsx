import React, { useEffect, useState } from 'react';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
    PieChart, Pie, Cell
} from 'recharts';
import { analytics } from '../services/api';
import { Card, CardContent, Typography, Grid, Box, CircularProgress, Alert } from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import AssignmentIcon from '@mui/icons-material/Assignment';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const AnalyticsDashboard = () => {
    const [monthlyData, setMonthlyData] = useState([]);
    const [statusData, setStatusData] = useState([]);
    const [approvalRate, setApprovalRate] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [monthlyRes, statusRes, approvalRes] = await Promise.all([
                    analytics.getMonthlyApplications(),
                    analytics.getStatusDistribution(),
                    analytics.getApprovalRate()
                ]);

                setMonthlyData(monthlyRes.data);

                // Transform status map to array for PieChart
                const statusArray = Object.entries(statusRes.data).map(([name, value]) => ({
                    name, value
                }));
                setStatusData(statusArray);

                setApprovalRate(approvalRes.data);
            } catch (err) {
                console.error("Error fetching analytics:", err);
                setError("Failed to load analytics data");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) return <Box display="flex" justifyContent="center" m={4}><CircularProgress /></Box>;
    if (error) return <Alert severity="error">{error}</Alert>;

    return (
        <Box sx={{ flexGrow: 1, p: 3 }}>
            <Typography variant="h4" gutterBottom component="div" sx={{ mb: 4, fontWeight: 'bold', color: '#1a237e' }}>
                Analytics Dashboard
            </Typography>

            {/* Metric Cards */}
            <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} md={4}>
                    <Card sx={{ bgcolor: '#e3f2fd' }}>
                        <CardContent>
                            <Box display="flex" alignItems="center">
                                <AssignmentIcon sx={{ fontSize: 40, color: '#1565c0', mr: 2 }} />
                                <div>
                                    <Typography color="textSecondary" gutterBottom>Total Applications</Typography>
                                    <Typography variant="h4">{approvalRate?.total || 0}</Typography>
                                </div>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item xs={12} md={4}>
                    <Card sx={{ bgcolor: '#e8f5e9' }}>
                        <CardContent>
                            <Box display="flex" alignItems="center">
                                <CheckCircleIcon sx={{ fontSize: 40, color: '#2e7d32', mr: 2 }} />
                                <div>
                                    <Typography color="textSecondary" gutterBottom>Approved</Typography>
                                    <Typography variant="h4">{approvalRate?.approved || 0}</Typography>
                                </div>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item xs={12} md={4}>
                    <Card sx={{ bgcolor: '#fff3e0' }}>
                        <CardContent>
                            <Box display="flex" alignItems="center">
                                <TrendingUpIcon sx={{ fontSize: 40, color: '#ef6c00', mr: 2 }} />
                                <div>
                                    <Typography color="textSecondary" gutterBottom>Approval Rate</Typography>
                                    <Typography variant="h4">{approvalRate?.rate?.toFixed(1)}%</Typography>
                                </div>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            <Grid container spacing={4}>
                {/* Monthly Applications Chart */}
                <Grid item xs={12} md={8}>
                    <Card sx={{ p: 2, height: '100%' }}>
                        <Typography variant="h6" gutterBottom>Applications Timeline</Typography>
                        <ResponsiveContainer width="100%" height={300}>
                            <LineChart data={monthlyData}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="month" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Line type="monotone" dataKey="applications" stroke="#8884d8" activeDot={{ r: 8 }} />
                                <Line type="monotone" dataKey="totalAmount" stroke="#82ca9d" />
                            </LineChart>
                        </ResponsiveContainer>
                    </Card>
                </Grid>

                {/* Status Distribution Chart */}
                <Grid item xs={12} md={4}>
                    <Card sx={{ p: 2, height: '100%' }}>
                        <Typography variant="h6" gutterBottom>Status Distribution</Typography>
                        <ResponsiveContainer width="100%" height={300}>
                            <PieChart>
                                <Pie
                                    data={statusData}
                                    cx="50%"
                                    cy="50%"
                                    labelLine={false}
                                    label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                                    outerRadius={80}
                                    fill="#8884d8"
                                    dataKey="value"
                                >
                                    {statusData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                    ))}
                                </Pie>
                                <Tooltip />
                            </PieChart>
                        </ResponsiveContainer>
                    </Card>
                </Grid>
            </Grid>
        </Box>
    );
};

export default AnalyticsDashboard;
