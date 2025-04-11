// src/main/client/src/components/chat/Chat.tsx
import React, { useState, useEffect } from 'react';
import { Message, createMessage, getMessages } from '../../api/MessageApi';
import { v4 as uuidv4 } from 'uuid';

interface ChatProps {
    incidentId: string;
}

const Chat: React.FC<ChatProps> = ({ incidentId }) => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [newMessage, setNewMessage] = useState('');

    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const messagesFetched = await getMessages(incidentId);
                setMessages(messagesFetched);
            } catch (error) {
                console.error('Chyba při načítání zpráv:', error);
            }
        };
        fetchMessages();
    }, [incidentId]);

    const handleSend = async () => {
        if (!newMessage.trim()) return;

        const userMsg: Message = {
            id: uuidv4(),
            incident: { id: incidentId },
            message: newMessage,
            sender: 'USER',
            timestamp: new Date().toISOString(),
        };

        setMessages(prev => [...prev, userMsg]);

        try {
            const aiResponse = await createMessage(userMsg);
            console.log(aiResponse);
            setMessages(prev => [...prev, aiResponse]);
        } catch (error) {
            console.error('Chyba při odesílání zprávy:', error);
        }

        setNewMessage('');
    };

    return (
        <div className="chat">
            <div className="chat-messages">
                {messages.map(msg => (
                    <div key={msg.id} className={`chat-message ${msg.sender === 'USER' ? 'user-message' : 'ai-message'}`}>
                        <div className="message-sender">{msg.sender}:</div>
                        <div className="message-text">{msg.message}</div>
                        <div className="message-timestamp">{msg.timestamp}</div>
                    </div>
                ))}
            </div>
            <div className="chat-input">
                <input
                    type="text"
                    placeholder="Napiš zprávu..."
                    value={newMessage}
                    onChange={e => setNewMessage(e.target.value)}
                />
                <button onClick={handleSend}>Odeslat</button>
            </div>
        </div>
    );
};

export default Chat;