// src/main/client/src/components/chat/Chat.tsx
import React, {useState, useEffect, useRef} from 'react';
import { Message, createMessage, getMessages } from '../../api/MessageApi';
import { v4 as uuidv4 } from 'uuid';
import './Chat.css';
import userImg from "../../assets/user.png";
import aiImg from "../../assets/robot.png"
import generateLocalTimestamp from "../../util/util.ts";
import { Button } from 'react-bootstrap';

interface ChatProps {
    incidentId: string;
}

const Chat: React.FC<ChatProps> = ({ incidentId }) => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [newMessage, setNewMessage] = useState('');
    const messagesEndRef = useRef<HTMLDivElement>(null);


    useEffect(() => {
        const fetchMessages = async () => {
            try {
                let messagesFetched = await getMessages(incidentId);
                messagesFetched.sort(
                    (a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());
                setMessages(messagesFetched);
            } catch (error) {
                console.error('Chyba při načítání zpráv:', error);
            }
        };
        void fetchMessages();
    }, [incidentId]);

    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]);

    const handleSend = async () => {
        if (!newMessage.trim()) return;

        const userMsg: Message = {
            id: uuidv4(),
            incident: { id: incidentId },
            message: newMessage,
            sender: 'USER',
            timestamp: generateLocalTimestamp(),
        };

        setMessages(prev => [...prev, userMsg]);

        try {
            const aiResponse = await createMessage(userMsg);
            setMessages(prev => [...prev, aiResponse]);
        } catch (error) {
            console.error('Chyba při odesílání zprávy:', error);
        }

        setNewMessage('');
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            void handleSend();
        }
    };

    return (
        <div className="chat">
            <div className="chat-messages">
                {messages.map(msg => (
                    <div key={msg.id} className={`chat-message ${msg.sender === 'USER' ? 'user-message' : 'ai-message'}`}>
                        {msg.sender === 'USER' ? (
                            <div className="message-content">
                                <span className="message-text">{msg.message}</span>
                                <span className="colon">:</span>
                                <img src={userImg} alt="user" className="message-icon" />
                            </div>
                        ) : (
                            <div className="message-content">
                                <img src={aiImg} alt="robot" className="message-icon" />
                                <span className="colon">:</span>
                                <span className="message-text">{msg.message}</span>
                            </div>
                        )}
                    </div>
                ))}
                <div ref={messagesEndRef} className="scroll-anchor" />
            </div>
            <div className="chat-input">
                <input
                    type="text"
                    placeholder="Napiš zprávu..."
                    value={newMessage}
                    onChange={e => setNewMessage(e.target.value)}
                    onKeyDown={handleKeyDown}
                />
                <Button variant="secondary" onClick={handleSend}>Odeslat</Button>
            </div>
        </div>
    );
};

export default Chat;