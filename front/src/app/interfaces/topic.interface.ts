export interface Topic {
    id: number;
    title: string;
    description: string;
    articles: number[];
    users_subscribed: number[];
    createdAt: Date;
    updatedAt?: Date;
}