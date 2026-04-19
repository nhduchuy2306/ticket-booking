import { Button, Progress, Tag } from "antd";
import React, { useEffect, useState } from "react";
import { FiArrowRight, FiClock, FiRefreshCw, FiShield, FiUsers } from "react-icons/fi";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { CustomerAuthStorage } from "../../services/CustomerAuth/CustomerAuthStorage.ts";
import { formatCountdown } from "../../utils/bookingSession.ts";
import "../auths/auth.scss";

const parsePositiveInteger = (value: string | null): number | null => {
    if (!value) {
        return null;
    }

    const parsedValue = Number(value);
    if (!Number.isFinite(parsedValue) || parsedValue <= 0) {
        return null;
    }

    return Math.floor(parsedValue);
};

const resolveDeadlineMs = (releaseAt: string | null, waitMinutes: number | null): number | null => {
    if (releaseAt) {
        const parsedReleaseAt = Date.parse(releaseAt);
        if (!Number.isNaN(parsedReleaseAt)) {
            return parsedReleaseAt;
        }
    }

    if (waitMinutes !== null) {
        return Date.now() + waitMinutes * 60 * 1000;
    }

    return null;
};

const WaitingRoomPage: React.FC = () => {
    const navigate = useNavigate();
    const {id: eventId} = useParams<{ id?: string }>();
    const [searchParams] = useSearchParams();
    const eventName = searchParams.get("eventName")?.trim() || searchParams.get("title")?.trim() || (eventId ? `Event #${eventId}` : "your concert");
    const queuePosition = parsePositiveInteger(searchParams.get("position"));
    const queueTotal = parsePositiveInteger(searchParams.get("total"));
    const waitMinutes = parsePositiveInteger(searchParams.get("waitMinutes"));
    const releaseAt = searchParams.get("releaseAt");
    const nextPath = searchParams.get("next")?.trim() || (eventId ? `/gyp/events/${eventId}/choose-seats` : null);
    const customerName = CustomerAuthStorage.getCustomerName();
    const [deadlineMs] = useState<number | null>(() => resolveDeadlineMs(releaseAt, waitMinutes));
    const [secondsLeft, setSecondsLeft] = useState<number>(() => {
        if (deadlineMs === null) {
            return waitMinutes !== null ? waitMinutes * 60 : 0;
        }

        return Math.max(0, Math.floor((deadlineMs - Date.now()) / 1000));
    });

    useEffect(() => {
        if (deadlineMs === null) {
            setSecondsLeft(waitMinutes !== null ? waitMinutes * 60 : 0);
            return;
        }

        const updateCountdown = () => {
            setSecondsLeft(Math.max(0, Math.floor((deadlineMs - Date.now()) / 1000)));
        };

        updateCountdown();
        const timer = window.setInterval(updateCountdown, 1000);

        return () => window.clearInterval(timer);
    }, [deadlineMs, waitMinutes]);

    const isReady = deadlineMs === null || secondsLeft === 0;
    const progressPercent = queuePosition !== null && queueTotal !== null
            ? Math.max(0, Math.min(100, Math.round(((queueTotal - queuePosition + 1) / queueTotal) * 100)))
            : null;
    const queueLabel = queuePosition !== null
            ? queueTotal !== null
                    ? `${queuePosition.toLocaleString()} / ${queueTotal.toLocaleString()}`
                    : `#${queuePosition.toLocaleString()}`
            : "Calculating";
    const etaLabel = isReady
            ? (nextPath ? "Now" : "Ready")
            : formatCountdown(secondsLeft);

    const handleContinue = () => {
        if (nextPath && isReady) {
            navigate(nextPath);
            return;
        }

        navigate(eventId ? `/gyp/events/${eventId}` : "/gyp/");
    };

    const handleRefresh = () => {
        window.location.reload();
    };

    const handleBackToEvent = () => {
        navigate(eventId ? `/gyp/events/${eventId}` : "/gyp/");
    };

    const handleHome = () => {
        navigate("/gyp/");
    };

    return (
            <div className="customer-auth-shell">
                <div className="customer-auth-orb customer-auth-orb-one"/>
                <div className="customer-auth-orb customer-auth-orb-two"/>

                <div className="customer-auth-grid">
                    <section className="customer-auth-hero">
                        <div>
                            <div className="customer-auth-brand">
                                <div className="customer-auth-brand-mark">GYP</div>
                                <div>
                                    <p className="customer-auth-kicker">Waiting room</p>
                                    <h1>
                                        {customerName ? `Hi ${customerName}, you are in line for ${eventName}` : `You are in line for ${eventName}`}
                                    </h1>
                                </div>
                            </div>

                            <p className="customer-auth-hero-copy">
                                Thousands of fans may try to buy at the same time. Keep this page open and we will
                                preserve your access path while the queue moves.
                            </p>
                        </div>

                        <div className="customer-auth-stat-grid">
                            <div className="customer-auth-stat-card">
                                <FiUsers/>
                                <span className="text-sm uppercase tracking-[0.16em] text-slate-400">Queue position</span>
                                <strong className="text-2xl text-white">{queueLabel}</strong>
                            </div>
                            <div className="customer-auth-stat-card">
                                <FiClock/>
                                <span className="text-sm uppercase tracking-[0.16em] text-slate-400">Estimated access</span>
                                <strong className="text-2xl text-white">{etaLabel}</strong>
                            </div>
                            <div className="customer-auth-stat-card">
                                <FiShield/>
                                <span className="text-sm uppercase tracking-[0.16em] text-slate-400">Session state</span>
                                <strong className="text-2xl text-white">{isReady ? "Ready to enter" : "Held securely"}</strong>
                            </div>
                        </div>

                        <div className="customer-auth-meta">
                            <span>Event access stays tied to this browser session.</span>
                            <span>{deadlineMs ? `Auto updates every second` : `Live queue information will appear when available`}</span>
                        </div>
                    </section>

                    <section className="customer-auth-card">
                        <div className="customer-auth-panel">
                            <div className="customer-auth-panel-heading flex items-start justify-between gap-4">
                                <div>
                                    <p className="customer-auth-kicker">Queue status</p>
                                    <h2>{isReady ? "Your turn is here" : "Please stay on this page"}</h2>
                                </div>
                                <Tag color={isReady ? "success" : "gold"}
                                     className="!m-0 !rounded-full !border-0 !px-3 !py-1 !font-semibold">
                                    {isReady ? "Ready" : "Waiting"}
                                </Tag>
                            </div>

                            <div className="mt-6 rounded-2xl border border-white/10 bg-slate-950/55 p-5">
                                <div className="flex items-end justify-between gap-4">
                                    <div>
                                        <p className="text-xs font-bold uppercase tracking-[0.22em] text-slate-400">Spot
                                            in line</p>
                                        <div className="mt-1 text-5xl font-black text-white">{queuePosition !== null ? queuePosition.toLocaleString() : "—"}</div>
                                    </div>
                                    <div className="text-right">
                                        <p className="text-xs font-bold uppercase tracking-[0.22em] text-slate-400">Time
                                            left</p>
                                        <div className="mt-1 text-3xl font-black text-amber-400">{isReady ? "00:00" : formatCountdown(secondsLeft)}</div>
                                    </div>
                                </div>

                                {progressPercent !== null ? (
                                        <Progress
                                                className="mt-5 [&_.ant-progress-outer]:!mr-0 [&_.ant-progress-bg]:!bg-[linear-gradient(90deg,#fbbf24_0%,#38bdf8_100%)]"
                                                percent={progressPercent}
                                                strokeLinecap="round"
                                                showInfo={false}
                                                trailColor="rgba(148, 163, 184, 0.16)"
                                        />
                                ) : (
                                        <div className="mt-5 h-2 overflow-hidden rounded-full bg-slate-800/80">
                                            <div className="h-full w-1/3 animate-pulse rounded-full bg-gradient-to-r from-amber-400 via-yellow-300 to-sky-400"/>
                                        </div>
                                )}

                                <p className="mt-4 text-sm leading-6 text-slate-300">
                                    {isReady
                                            ? "Your access window is ready. You can continue to the seat selection flow now."
                                            : "Keep this tab active. We will update the timer automatically and keep your booking context ready for the next step."}
                                </p>
                            </div>

                            <div className="mt-5 grid gap-3">
                                <Button
                                        type="primary"
                                        htmlType="button"
                                        className="h-12 rounded-xl border-0 bg-gradient-to-r from-amber-400 to-amber-500 font-extrabold tracking-wide text-slate-950 shadow-lg shadow-amber-500/20 hover:from-amber-300 hover:to-amber-500 hover:text-slate-950"
                                        onClick={handleContinue}
                                        disabled={!isReady && deadlineMs !== null}
                                        icon={<FiArrowRight/>}
                                        block
                                >
                                    {isReady ? (nextPath ? "Continue to ticket selection" : "Back to event") : `Available in ${formatCountdown(secondsLeft)}`}
                                </Button>

                                <Button
                                        htmlType="button"
                                        className="customer-google-button"
                                        onClick={handleRefresh}
                                        icon={<FiRefreshCw/>}
                                        block
                                >
                                    Refresh status
                                </Button>

                                <Button
                                        htmlType="button"
                                        className="h-12 rounded-xl border border-slate-600/30 bg-slate-900/92 font-semibold text-slate-100 hover:border-amber-400/40 hover:text-white"
                                        onClick={handleBackToEvent}
                                        block
                                >
                                    Back to event
                                </Button>

                                <Button
                                        htmlType="button"
                                        className="h-12 rounded-xl border border-slate-600/20 bg-transparent font-semibold text-slate-200 hover:border-slate-400/40 hover:text-white"
                                        onClick={handleHome}
                                        block
                                >
                                    Browse all events
                                </Button>
                            </div>

                            <div className="customer-auth-meta mt-5 rounded-2xl border border-white/10 bg-slate-950/45 px-4 py-3 text-sm text-slate-300/85">
                                <span>When your slot opens, you will be able to continue without logging in again.</span>
                                <span>{eventId ? `Event ID: ${eventId}` : "Queue access is shown in generic mode"}</span>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
    );
};

export default WaitingRoomPage;


